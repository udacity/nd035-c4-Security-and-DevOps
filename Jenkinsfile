node('docker') {
    checkout scm
    stage('Build') {
        docker.image('python:3.5.1').inside {
            sh 'python --version'
        }
    }
}
