package config

import "fmt"

type IncidentServiceConfig struct {
	Host string
	Port int
}

func GetIncidentServiceConfig() *IncidentServiceConfig {
	return &IncidentServiceConfig{
		Host: GetEnvStr("INCIDENT_SERVICE_HOST", "localhost"),
		Port: GetEnvInt("INCIDENT_SERVICE_PORT", 8081),
	}
}

func GetIncidentURI() string {
	return fmt.Sprintf(
		"http://%s:%d",
		GetIncidentServiceConfig().Host,
		GetIncidentServiceConfig().Port,
	)
}

func GetIncidentToken() string {
	return GetEnvStr("INCIDENT_SERVICE_API_TOKEN", "eyJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiIsImtpZCI6IjI3MWUxZjhjMTlmYmE3ZTRjOThiNjQzMTQ5ZmNhNjlkIn0.eyJpc3MiOiJtYTcwLndpZW4uZ3YuYXQiLCJzdWIiOiJtYXhtdXN0ZXJtYW5uQG1hNzAud2llbi5ndi5hdCIsIm5hbWUiOiJNYXggTXVzdGVybWFubiIsImF1ZCI6ImF0LmFzZS5yZXNwb25kIiwiaWF0IjoxNzExOTI5NjAwLCJleHAiOjE3MjI0NzA0MDAsInJlYWxtX2FjY2VzcyI6eyJyb2xlcyI6WyJjYWxsdGFrZXIiXX19.Pv7-ztts3CnP2tadoN6BWPMmJsEvqi70pWxxpJX2LRNBFK9m4B-TnyXCU0CRqBmZBDXwsFy6aPZU_AmtoOKqth-vKFd_q8c8hnFPmvIBYiGs9zzG_g0kKxgevo8TofKNtF5oHBDcr39FeXAqjVq6GxAUyk0h00iE3L3R6RrqWzxFEOt6M1DlgKN-fLWp8sqNbkKZJSysHomYpRQvxC1k72tVuVYab87O1_MUwkGJHKbGTu4bH9hscTDuUfadn3RxKfHtGa9lG3kXAPQVI29gpTtdGp_yKHmpR_dbUZX2nBXzFFG1UiGz67lLYebpsXWrAJEyZiBJ2UQIT3cywc-KtHOZszA0zNSu1BqAw-ItFYLzjtzEDpLeI9EZ5hnl8x-8wP6Ml20Y-dggBEosLAhr3h1Ux5rmPAU1H1thSW47Bud2inkBVSQxIg_ufVM8u5jiQOK_e4r2Z0BrRAOXjMLbypJJsxHAJF0RQDzocCb5OOuWTtnkvfpr-pbrCO_T83je")
}

func GetIncidentCount() int {
	return GetEnvInt("INCIDENT_COUNT", 50)
}
