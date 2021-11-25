pipeline {
	agent any
	stages ('build sin test') { 
		steps { 
			nodejs (nodeJSInstallationName: 'nodejs'){
				sh 'npm install'
				sh 'npm rebuild'
				sh 'npm run build --skip-test --if-present'
			}
		}
	}
	
	stage ('unitTest') {
		steps {
			nodejs (nodeJSInstallationName: 'nodejs'){
				sh 'npm run test: coverage && cp coverage/lcov.info icov.info || echo "Code coverage failed"'
				archiveArtifacts (artifacts: 'coverage/**', onlyIfSuccessful:true)
			}
		}
	}	
	
	stage ('deploy') {
		steps {
			nodejs (nodeJSInstallationName: 'nodejs')
				withAWS (credentials: 'aws-credentials'){
					sh 'serverless deploy'
				}
			}
		}
	}