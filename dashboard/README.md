# Dashboard 

## Requirements

- Node.js: use the installer available at [nodejs.org](http://nodejs.org/)


## How to run it locally
1. Run `npm install` inside the dashboard folder:
```bash
$ npm install
```

2. Start the dev server by running the command below. Navigate to `http://localhost:4200/`. The app will automatically reload if you change any of the source files.
```bash
$ ng serve
```

## Environments
The dashboard includes some environment files that change/replace the URLs of the employed services.

There are three different environments.
- mock - uses the developed mock interceptor
- development - uses the services started via docker-compose
- production - uses the URLs of the employed Kubernetes cluster

Use the configuration flag to change the environment when starting the application
```bash
$ ng serve --configuration=production
```

