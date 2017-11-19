pipeline {
  agent any
  
  stages {
    stage('Build') {
      sh './gradlew build'
      archiveArtifacts artifacts: '**/build/libs/*.jar', fingerprint: true
    }
    
    stage('Test') {
      sh './gradlew test'
    }
  }
}
