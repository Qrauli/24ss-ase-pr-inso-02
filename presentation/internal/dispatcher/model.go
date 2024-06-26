package dispatcher

type LocationCoordinates struct {
	Latitude  float64 `json:"latitude"`
	Longitude float64 `json:"longitude"`
}

type Resource struct {
	ID                  string              `json:"id"`
	Type                string              `json:"type"`
	State               string              `json:"state"`
	LocationCoordinates LocationCoordinates `json:"locationCoordinates"`
	AssignedIncident    string              `json:"assignedIncident"`
	UpdatedAt           string              `json:"updatedAt"`
}

type GeoResource struct {
	ID       string  `json:"resourceId"`
	Distance float64 `json:"distance"`
}
