node('java-agent') {
    timestamps {
        stage('Checkout') {
            git branch: 'master', url: 'https://github.com/sahkaa/jacocodemo.git'
        }
        stage('Compile and Test') {
            devfactory(env: 'dev', key: '126SABPM7GXEQDWRQUBA', secret: '8DNT9PTRVBM1I7DRKOOPZ9I4VHRFR9D54A5HT4L8', languages: 'Java', types: 'Java') {
                sh "mvn clean install"
                step( [ $class: 'JacocoPublisher' ] )
            }
        }
    }
}
