pipeline {
  agent any
  stages {
    stage('build') {
      steps {
        sh 'apk add maven'
        sh 'mvn --version'
      }
    }

  }
}