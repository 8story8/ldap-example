#!/bin/bash

SCRIPT_HOME="$( cd "$(dirname "$0")" ; pwd -P )"

LDAP_VERSION="1.4.0"
LDAP_IMAGE_ID=$(docker images -q osixia/openldap:$LDAP_VERSION)

function remove_ldap {
  docker rm -f ldap
}

function pull_ldap {
  docker pull osixia/openldap:$LDAP_VERSION
}

function init_ldap {
  cp $SCRIPT_HOME/environment/env.startup.yaml $SCRIPT_HOME/environment/config
  cp $SCRIPT_HOME/environment/init.ldif $SCRIPT_HOME/init
  docker create --name ldap \
    -v $SCRIPT_HOME/environment/config:/container/environment/01-custom \
    -v $SCRIPT_HOME/init:/container/service/slapd/assets/config/bootstrap/ldif/custom\
    -p 389:389 -p 636:636 \
    osixia/openldap:$LDAP_VERSION --loglevel debug --copy-service
  docker start ldap
}

function restart_ldap {
  docker restart ldap
}

# LDAP Image가 없을 경우
if [ -z $LDAP_IMAGE_ID ]; then
  pull_ldap
  init_ldap
# LDAP Image가 있을 경우
else
  LDAP_CONTAINER_ID=$(docker ps -aqf "name=ldap")

  # LDAP이 구동 중이지 않은 경우
  if [ -z $LDAP_CONTAINER_ID ]; then
    init_ldap
  # LDAP이 구동 중인 경우
  else
    LDAP_CONTAINER_EXIT_CODE=$(docker inspect $LDAP_CONTAINER_ID --format='{{.State.ExitCode}}')
    # LDAP Container에 오류가 발생했을 경우
    if [ $LDAP_CONTAINER_EXIT_CODE -ne 0 ]; then
      remove_ldap
      init_ldap
    # LDAP Container에 오류가 발생하지 않았을 경우
    else
      restart_ldap
    fi
  fi
fi
