pipeline {
  agent {
    docker {
      image 'maven-ci:latest'
    }

  }
  stages {
    stage('build') {
      steps {
        sh 'mvn --version'
      }
    }

  }
}