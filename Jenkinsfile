pipeline {
  agent any
  
  stages {
    stage('Build') {
      steps {
        sh 'chmod +x ./gradlew'
        sh './gradlew build'
        archiveArtifacts artifacts: '**/build/libs/*.jar', fingerprint: true
      }
    }
    
    stage('Test') {
      steps {
        sh './gradlew test'
      }
    }
  }
}
