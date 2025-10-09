pipeline {
    agent any
    
    environment {
        APP_NAME = 'spring-boot-crud'
        APP_PORT = '9091'
        JAR_FILE = 'target/demo-0.0.1-SNAPSHOT-exec.jar'
    }
    
    stages {
        stage('Verify Project Structure') {
            steps {
                script {
                    sh '''
                        echo "🔍 Verifying project structure..."
                        ls -la
                        [ -f Dockerfile ] && echo "✅ Dockerfile exists" || echo "❌ Dockerfile missing"
                        [ -f docker-compose.yml ] && echo "✅ docker-compose.yml exists" || echo "❌ docker-compose.yml missing"
                        [ -f pom.xml ] && echo "✅ pom.xml exists" || echo "❌ pom.xml missing"
                        [ -d src ] && echo "✅ src directory exists" || echo "❌ src directory missing"
                    '''
                }
            }
        }
        
        stage('Checkout Code') {
            steps {
                git branch: 'main', 
                url: 'https://github.com/isc-eulo/Demo-spring-boot-app.git'
            }
        }
        
    stage('Build and Test') {
    steps {
        sh 'mvn clean package -DskipTests'
    }
    post {
        always {
            // Archivar SOLO el JAR ejecutable (el bueno)
            archiveArtifacts 'target/*-exec.jar'
            
            // Opcional: Si quieres mantener junit pero que no falle
            junit allowEmptyResults: true, testResults: '**/target/surefire-reports/*.xml'
        }
    }
}
        
        stage('Verify JAR File') {
            steps {
                script {
                    sh """
                        echo "🔍 Checking JAR files..."
                        ls -la target/*.jar
                        echo "Using JAR: ${JAR_FILE}"
                        # Verificar que el JAR ejecutable existe
                        if [ -f "${JAR_FILE}" ]; then
                            echo "✅ JAR ejecutable encontrado"
                        else
                            echo "❌ JAR ejecutable no encontrado"
                            exit 1
                        fi
                    """
                }
            }
        }
        
        stage('Build Docker Image') {
            steps {
                script {
                    sh '''
                        echo "🐳 Building Docker image..."
                        docker build -t ${APP_NAME}:latest .
                        echo "✅ Docker image built successfully"
                        docker images | grep ${APP_NAME}
                    '''
                }
            }
        }
        
        stage('Deploy Application') {
            steps {
                script {
                    sh '''
                        echo "🚀 Deploying application..."
                        docker-compose down || true
                        docker-compose up -d --build
                        
                        # Esperar a que la aplicación inicie
                        echo "⏳ Waiting for application to start..."
                        sleep 30
                    '''
                }
            }
        }
        
        stage('Verify Deployment') {
            steps {
                script {
                    sh """
                        echo "🔍 Verifying deployment..."
                        # Probar múltiples endpoints
                        if curl -s -f http://localhost:${APP_PORT}/h2-console >/dev/null 2>&1; then
                            echo "✅ H2 Console is accessible"
                        elif curl -s -f http://localhost:${APP_PORT}/swagger-ui/index.html >/dev/null 2>&1; then
                            echo "✅ Swagger UI is accessible"
                        elif curl -s -f http://localhost:${APP_PORT}/v3/api-docs >/dev/null 2>&1; then
                            echo "✅ API Docs are accessible"
                        else
                            echo "⚠️  Testing raw port connectivity..."
                            if nc -z localhost ${APP_PORT} 2>/dev/null; then
                                echo "✅ Port ${APP_PORT} is open - application is running"
                            else
                                echo "❌ Application not responding"
                                docker-compose logs
                                exit 1
                            fi
                        fi
                    """
                }
            }
        }
    }
    
    post {
        always {
            sh """
                echo "=== Deployment Summary ==="
                echo "Application: ${APP_NAME}"
                echo "Port: ${APP_PORT}"
                docker ps --filter "name=app" --format "table {{.Names}}\\t{{.Status}}\\t{{.Ports}}"
            """
        }
        success {
            echo "🎉 Pipeline ejecutado exitosamente!"
            sh """
                echo ''
                echo '🌐 APPLICATION URLs:'
                echo '   Main:      http://localhost:${APP_PORT}'
                echo '   H2:        http://localhost:${APP_PORT}/h2-console'
                echo '   Swagger:   http://localhost:${APP_PORT}/swagger-ui/index.html'
                echo '   API Docs:  http://localhost:${APP_PORT}/v3/api-docs'
                echo ''
                echo '📝 Para usar la API:'
                echo '   1. Registrar usuario: POST /api/auth/register'
                echo '   2. Login: POST /api/auth/login'
                echo '   3. Usar token JWT en requests'
            """
        }
        failure {
            echo "❌ Pipeline falló"
            sh 'docker-compose logs || true'
        }
    }
}
