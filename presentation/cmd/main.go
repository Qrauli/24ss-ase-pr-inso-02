package main

import (
	"fmt"
	"presentation/config"
	"presentation/internal/dispatcher"
	"presentation/internal/incident"
)

func main() {
	addPresentationData()
}

func addPresentationData() {
	incidentClient := incident.NewIncidentServiceClient(
		config.GetIncidentURI(),
		config.GetIncidentToken(),
	)

	// Check if presentation data is already here
	incidents, err := incidentClient.GetIncidents()
	if err != nil {
		fmt.Printf("Failed to fetch incidents: %v\n", err)
		return
	}

	if len(incidents) != 0 {
		fmt.Println("Presentation data already exists, nothing to do...")
		return
	}

	fmt.Printf("No presentation data found, adding presentation data...\n")

	incidentsGenerator := incident.NewIncidentGenerator(config.GetFilePath())

	var incidentIds []string
	for i := 0; i < config.GetIncidentCount(); i++ {
		id, err := incidentClient.CreateIncident(incidentsGenerator.GenerateRandomIncident())
		if err != nil {
			fmt.Printf("Failed to create incident: %v\n", err)
			return
		}
		incidentIds = append(incidentIds, id)
	}

	dispatcherClient := dispatcher.NewDispatcherServiceClient(
		config.GetDispatcherURI(),
		config.GetDispatcherToken(),
	)

	resources, err := dispatcherClient.GetResources()
	if err != nil {
		fmt.Printf("Failed to fetch resources: %v\n", err)
		return
	}

	// Assign resources to incidents
	for i := 0; i < min(len(resources), len(incidentIds))/2; i++ {
		recommendedResources, err := dispatcherClient.GetRecommendedResources(incidentIds[i])
		if err != nil {
			fmt.Printf("Failed to fetch recommended resources: %v\n", err)
			return
		}

		// Assign each recommended resource to the incident
		for _, resource := range recommendedResources {
			_, err = dispatcherClient.AssignResourceToIncident(resource.ID, incidentIds[i])
			if err != nil {
				fmt.Printf("Failed to assign resource to incident: %v\n", err)
				return
			}
		}
	}

	fmt.Printf("Successfully added presentation data!")
}
