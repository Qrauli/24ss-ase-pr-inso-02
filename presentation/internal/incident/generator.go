package incident

import (
	"encoding/json"
	"fmt"
	"io"
	"log"
	"math/rand"
	"os"
)

type Generator struct {
	FilePath string
	Codes    []string
}

func NewIncidentGenerator(filePath string) *Generator {
	return &Generator{
		FilePath: filePath,
		Codes:    getSupportedCodes(filePath),
	}
}

func (g *Generator) GenerateRandomIncident() Incident {
	address := LocationAddress{
		Street:                generateRandomStreet(),
		PostalCode:            generateRandomPostalCode(),
		City:                  "Vienna",
		AdditionalInformation: generateRandomAdditionalInformation(),
	}
	coordinates := getRandomCoordinates()

	return Incident{
		CallerNumber: generateRandomPhoneNumber(),
		Patients: []Patient{
			{
				Age: rand.Intn(100),
				Sex: generateRandomSex(),
			},
		},
		NumberOfPatients: 1,
		Code:             g.Codes[rand.Intn(len(g.Codes))],
		State:            "READY",
		Location: Location{
			Address:     address,
			Coordinates: coordinates,
		},
	}
}

func generateRandomStreet() string {
	streets := []string{
		"Main Street",
		"First Street",
		"Second Street",
		"Third Street",
		"Fourth Street",
	}
	return fmt.Sprintf("%s %d", streets[rand.Intn(len(streets))], rand.Intn(100))
}

func generateRandomPostalCode() string {
	return fmt.Sprintf("1%d%d0", rand.Intn(2), rand.Intn(9))
}

func generateRandomPhoneNumber() string {
	return fmt.Sprintf("+43-%d-%d-%d-%d", rand.Intn(999), rand.Intn(999), rand.Intn(999), rand.Intn(99))
}

func generateRandomSex() string {
	sex := []string{
		"MALE",
		"FEMALE",
		"UNKNOWN",
	}
	return sex[rand.Intn(len(sex))]
}

func generateRandomAdditionalInformation() string {
	infos := []string{
		"Be aware of the dog!",
		"Second floor",
		"Tramway station nearby",
	}
	return infos[rand.Intn(len(infos))]
}

func getRandomCoordinates() LocationCoordinates {
	// Vienna coordinates (approximate
	latitude := 48.12026650742792 + rand.Float64()*(48.32864301922961-48.12026650742792)
	longitude := 16.186381219544284 + rand.Float64()*(16.54451160900846-16.186381219544284)
	return LocationCoordinates{
		Latitude:  latitude,
		Longitude: longitude,
	}

}

func getSupportedCodes(filePath string) []string {
	file, err := os.Open(filePath)
	if err != nil {
		log.Fatalf("Failed to open file: %v\n", err)
		return nil
	}
	defer func(file *os.File) {
		err := file.Close()
		if err != nil {
			log.Fatalf("Failed to close file: %v\n", err)
		}
	}(file)

	bytes, err := io.ReadAll(file)
	if err != nil {
		log.Fatalf("Failed to read file: %v\n", err)
		return nil
	}

	var regulations []Regulations
	err = json.Unmarshal(bytes, &regulations)
	if err != nil {
		log.Fatalf("Failed to unmarshal JSON: %v\n", err)
		return nil
	}

	var codes []string
	for _, regulation := range regulations {
		codes = append(codes, regulation.Code)
	}
	return codes
}
