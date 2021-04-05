pipeline {
  agent {
    docker {
      image 'maven-ci:latest'
    }

  }
  stages {
    stage('build') {
      steps {
        sh 'apk add maven'
        sh 'mvn --version'
      }
    }

  }
}