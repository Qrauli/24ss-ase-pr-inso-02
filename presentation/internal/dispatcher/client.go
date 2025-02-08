package dispatcher

import (
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

func NewDispatcherServiceClient(url string, token string) *RestClient {
	return &RestClient{
		URL:   url,
		Token: token,
	}
}

func (c *RestClient) GetResources() ([]Resource, error) {
	req, err := http.NewRequest("GET", c.URL+"/resources", nil)
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

	var result []Resource
	err = json.Unmarshal(body, &result)
	if err != nil {
		return nil, err
	}

	log.Printf("Fetched all (%d) resources", len(result))

	return result, nil
}

func (c *RestClient) GetRecommendedResources(incidentId string) ([]GeoResource, error) {
	req, err := http.NewRequest("GET", c.URL+"/incidents/"+incidentId+"/recommendations", nil)
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

	var result []GeoResource
	err = json.Unmarshal(body, &result)
	if err != nil {
		return nil, err
	}

	log.Printf("Fetched recommendations for incident %s", incidentId)

	return result, nil
}

func (c *RestClient) AssignResourceToIncident(resourceId string, incidentId string) (*Resource, error) {
	req, err := http.NewRequest("POST", c.URL+"/resources/"+resourceId+"/assign/"+incidentId, nil)
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

	var result Resource
	err = json.Unmarshal(body, &result)
	if err != nil {
		return nil, err
	}

	log.Printf("Assigned resource %s to incident %s", resourceId, incidentId)

	return &result, nil
}
