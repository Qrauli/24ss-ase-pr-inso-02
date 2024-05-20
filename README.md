# RESPOND

## How to Run

### Private Package Registry Access

The backend microservices depend on the common-lib stored in the GitLab private package registry.
To build and run the project, you need to provide a private access token.

- Navigate to the [GitLab Access Tokens](https://reset.inso.tuwien.ac.at/repo/-/user_settings/personal_access_tokens) page.
- Generate a new access token with the `read_api` and `read_registry` scopes.
- Set the `CI_JOB_TOKEN` environment variable to your generated token. You can store it in the `.env` file in the docker directory, but be careful not to commit it to git if you do so!

### Build and run

First, navigate to the docker directory. Then, build the project:

```bash
docker-compose build
```

Finally, run the project:

```bash
docker-compose up -d
```

## Team Members

- Lucas Gugler (Usability Engineer)
- Hannes Hauer (DevOps Engineer)
- Manuel Oberbacher (Requirements Engineer)
- Sophia Schober (Test Coordinator)
- Philipp Slowak (Technical Architect)
- Alexander Woda (Project Coordinator)
