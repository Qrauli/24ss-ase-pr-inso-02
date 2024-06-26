package incident

import (
	"bytes"
	"encoding/json"
	"fmt"
	"io"
	"log"
	"net/http"
)

type RestClient struct {
	URL   string
	Token string
}

func NewIncidentServiceClient(url string, token string) *RestClient {
	return &RestClient{
		URL:   url,
		Token: token,
	}
}

func (c *RestClient) GetIncidents() ([]Incident, error) {
	req, err := http.NewRequest("GET", c.URL+"/incidents", nil)
	if err != nil {
		return nil, err
	}

	req.Header.Set("Authorization", "Bearer "+c.Token)
	req.Header.Set("Accept", "application/json")

	resp, err := http.DefaultClient.Do(req)
	if err != nil {
		return nil, err
	}
	defer func(Body io.ReadCloser) {
		err := Body.Close()
		if err != nil {
			log.Fatal("Failed to close response body")
		}
	}(resp.Body)

	if resp.StatusCode != http.StatusOK {
		return nil, fmt.Errorf("unexpected status code: %d", resp.StatusCode)
	}

	body, err := io.ReadAll(resp.Body)
	if err != nil {
		return nil, err

	}

	var result []Incident
	err = json.Unmarshal(body, &result)
	if err != nil {
		return nil, err
	}

	log.Printf("Fetched all (%d) incidents", len(result))

	return result, nil
}

func (c *RestClient) CreateIncident(incident Incident) (string, error) {
	payload, err := json.Marshal(incident)
	if err != nil {
		return "", err
	}

	req, err := http.NewRequest("POST", c.URL+"/incidents", bytes.NewBuffer(payload))
	if err != nil {
		return "", err
	}

	req.Header.Set("Authorization", "Bearer "+c.Token)
	req.Header.Set("Content-Type", "application/json")

	resp, err := http.DefaultClient.Do(req)
	if err != nil {
		return "", err
	}
	defer func(Body io.ReadCloser) {
		err := Body.Close()
		if err != nil {
			log.Fatal("Failed to close response body")
		}
	}(resp.Body)

	if resp.StatusCode != http.StatusCreated {
		return "", fmt.Errorf("unexpected status code: %d", resp.StatusCode)
	}

	body, err := io.ReadAll(resp.Body)
	if err != nil {
		return "", err
	}

	// Trim the first and last character (quotes)
	id := string(body)
	id = id[1 : len(id)-1]

	log.Printf("Created incident with ID: %s", id)

	return id, nil
}
