pipeline {
  agent {
    docker {
      image 'maven:3-alpine'
      args '-u root'
    }

  }
  stages {
    stage('build') {
      steps {
        sh 'echo ls -a'
        sh 'mvn -v'
        sh 'mvn compile'
      }
    }

  }
}
