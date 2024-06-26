package config

import (
	"log"
	"os"
	"strconv"
)

// GetEnvStr retrieves an environment variable, or a default value if the variable is undefined.
func GetEnvStr(key string, defaultValue string) string {
	value, ok := os.LookupEnv(key)
	if !ok {
		return defaultValue
	}
	return value
}

// GetEnvInt retrieves an environment variable as an integer, or a default value if the variable is undefined.
func GetEnvInt(key string, defaultValue int) int {
	value, ok := os.LookupEnv(key)
	if !ok {
		return defaultValue
	}
	valueInt, err := strconv.Atoi(value)
	if err != nil {
		log.Fatalf("Failed to parse %s as int: %v", key, err)
		return defaultValue
	}
	return valueInt
}
