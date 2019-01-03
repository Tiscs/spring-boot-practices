# Spring Cloud Practices

## Components

- scp-parent:  
    Public Parent Project
- scp-config:  
    Spring Cloud Config Server
- scp-users:  
    Spring Cloud OAuth2 Server


## Docker Images


- scp-config:
    ``` sh
    $ docker build --rm -f scp-config/Dockerfile .
    ```

- scp-users:
    ``` sh
    $ docker build --rm -f scp-users/Dockerfile .
    ```