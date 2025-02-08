
# Stage 1: Compile and Build angular codebase

# Use official node image as the base image
FROM node:latest as build

ARG BUILD_ENV=prod
# Set the working directory
WORKDIR /usr/local/app

# Add the source code to app
COPY ./ /usr/local/app/

# Install all the dependencies
RUN npm install

# Generate the build of the application
RUN npm run build:${BUILD_ENV}


# Stage 2: Serve app with nginx server

# Use official nginx image as the base image
FROM nginxinc/nginx-unprivileged:latest

# Copy the build output to replace the default nginx contents.
COPY --from=build /usr/local/app/dist/respond/browser /usr/share/nginx/html
COPY /nginx.conf  /etc/nginx/conf.d/default.conf

EXPOSE 80
