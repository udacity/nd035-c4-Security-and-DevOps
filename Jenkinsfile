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
        checkout scm
        sh 'ls -a'
        sh 'mvn -v'
        sh 'cd starter_code'
        sh 'mvn -f starter_code/pom.xml -B  -X -DskipTests clean compile package'
      }
    }
    stage('test') {
          steps {
            sh 'mvn -f starter_code/pom.xml test'
          }
   }
    stage ('deploy') {
      environment {
        TOMCAT_URL = 'http://3.122.192.28:8080'
        TOMCAT_CREDENTIALS_ID = 'tomcat'
        TOMCAT_CONTEXT_PATH = 'auth-course'
      }
      steps {
        deploy adapters: [tomcat9(url: "$TOMCAT_URL", credentialsId: "$TOMCAT_CREDENTIALS_ID")],
                          war: '**/*.war',
                         contextPath: "$TOMCAT_CONTEXT_PATH"
      }
    }
  }
}
