pipeline {
  agent {
    docker {
      image 'maven:3.8-openjdk-15'
    }
  }
  environment {
    POM_PATH = 'starter_code/pom.xml'
  }
  stages {
    stage('build') {
      steps {
        checkout scm
        sh 'mvn -f $POM_PATH -B -DskipTests clean package'
      }
    }
    stage('test') {
          steps {
            sh 'mvn -f $POM_PATH test'
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
