#!/bin/bash

set -xeuo pipefail

#chown -R "$(id -u):$(id -g)" data extensions logs temp
chmod -R 700 "$SONARQUBE_HOME/data" "$SONARQUBE_HOME/extensions" "$SONARQUBE_HOME/logs" "$SONARQUBE_HOME/temp"

exec "$@"
