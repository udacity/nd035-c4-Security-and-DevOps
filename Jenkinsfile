pipeline {
  agent {
    docker {
      image 'maven-ci:latest'
    }

  }
  stages {
    stage('build') {
      steps {
        sh 'echo "finally Hello World?"'
      }
    }

  }
}
