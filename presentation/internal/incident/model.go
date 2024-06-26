package incident

type LocationAddress struct {
	Street                string `json:"street"`
	PostalCode            string `json:"postalCode"`
	City                  string `json:"city"`
	AdditionalInformation string `json:"additionalInformation"`
}

type LocationCoordinates struct {
	Latitude  float64 `json:"latitude"`
	Longitude float64 `json:"longitude"`
}

type Location struct {
	Address     LocationAddress     `json:"address"`
	Coordinates LocationCoordinates `json:"coordinates"`
}

type Patient struct {
	Age int    `json:"age"`
	Sex string `json:"sex"`
}

type Incident struct {
	ID               string    `json:"id"`
	CallerNumber     string    `json:"callerNumber"`
	Patients         []Patient `json:"patients"`
	Location         Location  `json:"location"`
	NumberOfPatients int       `json:"numberOfPatients"`
	Code             string    `json:"code"`
	State            string    `json:"state"`
}

type Regulations struct {
	Code      string   `json:"code"`
	Resources []string `json:"resources"`
}
