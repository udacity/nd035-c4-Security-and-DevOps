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
      steps {
        deploy adapters: [tomcat9(url: 'http://3.122.192.28:8080', credentialsId: 'tomcat')],
                          war: '**/*.war',
                         contextPath: 'auth-course'
      }
    }
  }
}
