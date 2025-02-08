package config

type RegulationsConfig struct {
	FilePath string
}

func GetRegulationsConfig() *RegulationsConfig {
	return &RegulationsConfig{
		FilePath: GetEnvStr("REGULATIONS_FILE_PATH", "regulations.json"),
	}
}

func GetFilePath() string {
	return GetRegulationsConfig().FilePath
}
