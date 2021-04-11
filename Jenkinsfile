pipeline {
  agent {
    docker {
      image 'maven:3.8-openjdk-15'
      args '-u root -v /root/.m2:/root/.m2'
    }

  }
  stages {
    stage('build') {
      steps {
        sh 'ls -a'
        sh 'mvn -v'
        sh 'cd starter_code'
        sh 'mvn -f starter_code/pom.xml -B  -X -DskipTests clean package'
      }
    }
    stage('test') {
          steps {
            sh 'mvn -f starter_code/pom.xml test'
          }
          post {
                always {
                    junit 'target/surefire-reports/*.xml'
                }
          }
          }
  }
}
