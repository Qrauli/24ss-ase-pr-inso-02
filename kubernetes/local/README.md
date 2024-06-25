## Local kubernetes testing

In the following, the "microservices" refers to the dashboard, incident, categorization and dispatcher service

### Steps
- if you want to test the logging setup, make sure you have a volume with storage class "local-path", access mode ReadWriteOnce and 1GB storage configured on your local kubernetes (I can show you how to do that in Rancher-Desktop)
- build the dashboard image locally with `docker build -t <image-name>:<tag> <path-to-dir> --build-arg CI_JOB_TOKEN="token" --build-arg BUILD_ENV=dev --build-arg CI_PROJECT_ID=4055` to activate internal request routing to localhost urls
- build all other microservice images locally with `docker build -t <image-name>:<tag> <path-to-dir> --build-arg CI_JOB_TOKEN="token" --build-arg CI_PROJECT_ID=4055`, path-to-dir must point to the directory that holds the corresponding Dockerfile, the CI-token is needed to pull the common-lib
- find the yaml configs in /kubernetes/local
- set the locally built image names in the microservice yamls, make sure that imagePullPolicy is set to "Never"
- make sure that "``SPRING_SECURITY_OAUTH2_RESOURCESERVER_JWT_JWK_SET_URI: 'http://s-24ss-ase-pr-inso-02-dashboard-service/assets/jwks.json'`` is set in the microservice deployment yamls
- port-forward the service ports of the different microservices in kubernetes with ``kubectl port-forward <pod-name> <port>`` (e.g. ``kubectl port-forward dashboard-service-7d24352-1324341 80``)
- Access the dashboard at `http://localhost:80/`