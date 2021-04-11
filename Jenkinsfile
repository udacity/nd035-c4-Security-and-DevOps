pipeline {
  agent {
    docker {
      image 'maven:3-alpine'
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
