#!/bin/bash

SCRIPT_HOME="$( cd "$(dirname "$0")" ; pwd -P )"

LDAP_VERSION="1.4.0"
LDAP_IMAGE_ID=$(docker images -q osixia/openldap:$LDAP_VERSION)

# LDAP Image가 있을 경우
if [ ! -z $LDAP_IMAGE_ID ]; then
  LDAP_CONTAINER_ID=$(docker ps -aqf "name=ldap")

  # LDAP이 구동 중이지 않은 경우
  if [ -z $LDAP_CONTAINER_ID ]; then
    docker rmi $LDAP_IMAGE_ID
  # LDAP이 구동 중인 경우
  else
    docker rm -f $LDAP_CONTAINER_ID
    docker rmi $LDAP_IMAGE_ID
  fi
fi
