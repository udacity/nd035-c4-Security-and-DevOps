pipeline {
    agent any

    stages {
        stage('Checkout') {
            steps {
                echo "Clone source from Github"
                checkout scm
            }
        }

        stage('Build') {
            steps {
                echo "Start maven build project"
                dir('./starter_code/') {
                    sh "./mvnw clean package -Dmaven.test.skip=true"
                }
            }
        }

        stage('Deploy to Tomcat') {
            steps {
                sh "rm -rf /tmp/warfile/*.war"
                sh "mv ./starter_code/target/auth.war /tmp/warfile/auth.war"
            }
        }
    }
}
