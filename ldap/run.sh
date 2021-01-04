#!/bin/bash

SCRIPT_HOME="$( cd "$(dirname "$0")" ; pwd -P )"

LDAP_IMAGE_ID=$(docker images -q osixia/openldap:1.4.0)

function remove_ldap {
}

function init_ldap {
  docker pull osixia/openldap:1.4.0
}

# Aergo Image가 없을 경우
if [ -z $LDAP_IMAGE_ID ]; then
  remove_ldap
  init_ldap

# LDAP Image가 있을 경우
else
  LDAP_CONTAINER_ID=$(docker ps -aqf "name=ldap")

  # LDAP이 구동 중이지 않은 경우
  if [ -z $LDAP_CONTAINER_ID ]; then
    remove_ldap
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
      docker restart ldap
    fi
  fi
fi
