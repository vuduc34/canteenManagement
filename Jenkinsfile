pipeline {
    agent any
    environment {
        BACKEND_DIR = "backend"
        FRONTEND_DIR = "frontend"
    }
    stages {
        stage('Checkout Code') {
            steps {
                script {
                    checkout scm
                }
            }
        }

        stage('Deploy to Server') {
            steps {
                script {
                    if (env.BRANCH_NAME == 'dev') {
                        deployToDevServer()
                    } else if (env.BRANCH_NAME == 'main') {
                        deployToProdServer()
                    } else {
                        error "Unknown branch, cannot determine deployment server!"
                    }
                }
            }
        }
    }
}

def deployToDevServer() {
    def SERVER_IP = 'ubuntu@13.55.218.9' // Dev Server IP
    sshagent(['jenkins-ssh-key']) {
       sh """
    ssh -o StrictHostKeyChecking=no ${SERVER_IP} "
        cd /home/ubuntu/canteenManagement && \
        git pull origin dev && \
        
        # Build Backend (Dev Mode)
        cd backend && ./mvnw clean package -DskipTests && \
        sudo systemctl restart springboot && \
        
        # Build Frontend (Dev Mode)
        cd /home/ubuntu/canteenManagement/frontend && \
        npm install && \
        ng build --configuration=production && \
        export NODE_OPTIONS='--max-old-space-size=4096' && \
        sudo cp -r dist/sakai-ng/* /var/www/frontend/ && \
        sudo systemctl restart nginx
    "
"""

    }
}

def deployToProdServer() {
    def SERVER_IP = 'ubuntu@3.107.198.234' // Prod Server IP
    sshagent(['jenkins-ssh-key']) {
       sh """
    ssh -o StrictHostKeyChecking=no ${SERVER_IP} "
        cd /home/ubuntu/canteenManagement && \
        git pull origin main && \
        
        # Build Backend (Prod Mode)
        cd backend && ./mvnw clean package -DskipTests && \
        sudo systemctl restart springboot && \
        
        # Build Frontend (Prod Mode)
        cd /home/ubuntu/canteenManagement/frontend && \
        npm install && \
        ng build && \
        export NODE_OPTIONS='--max-old-space-size=4096' && \
        sudo cp -r dist/sakai-ng/* /var/www/frontend/ && \
        sudo systemctl restart nginx
    "
"""

    }
}
