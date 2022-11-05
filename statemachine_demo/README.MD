Spring State Machine

- Maven pom using Spring Boot

```
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
	xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 https://maven.apache.org/xsd/maven-4.0.0.xsd">
	<modelVersion>4.0.0</modelVersion>
	<parent>
		<groupId>org.springframework.boot</groupId>
		<artifactId>spring-boot-starter-parent</artifactId>
		<version>2.7.4</version>
		<relativePath/> <!-- lookup parent from repository -->
	</parent>
	<groupId>com.example.sprintboot</groupId>
	<artifactId>statemachine_demo</artifactId>
	<version>0.0.1-SNAPSHOT</version>
	<name>statemachine_demo</name>
	<description>Demo project for Spring Boot</description>
	<properties>
		<java.version>11</java.version>
		<spring-statemachine.version>2.2.3.RELEASE</spring-statemachine.version>
	</properties>
	<dependencies>
		<dependency>
			<groupId>org.springframework.boot</groupId>
			<artifactId>spring-boot-starter-web</artifactId>
		</dependency>
		<dependency>
			<groupId>org.springframework.statemachine</groupId>
			<artifactId>spring-statemachine-starter</artifactId>
		</dependency>
		<dependency>
			<groupId>org.springframework.boot</groupId>
			<artifactId>spring-boot-devtools</artifactId>
			<scope>runtime</scope>
			<optional>true</optional>
		</dependency>
		<dependency>
			<groupId>org.springframework.boot</groupId>
			<artifactId>spring-boot-starter-test</artifactId>
			<scope>test</scope>
		</dependency>
	</dependencies>
	<dependencyManagement>
		<dependencies>
			<dependency>
				<groupId>org.springframework.statemachine</groupId>
				<artifactId>spring-statemachine-bom</artifactId>
				<version>${spring-statemachine.version}</version>
				<type>pom</type>
				<scope>import</scope>
			</dependency>
		</dependencies>
	</dependencyManagement>
	<build>
		<plugins>
			<plugin>
				<groupId>org.springframework.boot</groupId>
				<artifactId>spring-boot-maven-plugin</artifactId>
			</plugin>
		</plugins>
	</build>

</project>

```

- Spring Boot without web - plain java app
    - implements CommandLineRunner and override run()

    ```
    @SpringBootApplication
    public class Application implements CommandLineRunner {

        public static void main(String[] args) {
            SpringApplication.run(Application.class, args);
        }

    }
    ```

- States and events - use String or Enum (better)

```
public enum States {
    SI, S1, S2
}

public enum Events {
    E1, E2
}

```

- Spring state machine configuration
    - Use two Spring enabler annotations for configuration: 
        - @EnableStateMachine 
            - use @EnableStateMachine when you need a configuration to create an instance of StateMachine. 
        - @EnableStateMachineFactory
            - use @EnableStateMachineFactory when you need a configuration to create an instance of a StateMachineFactory.
    - These annotations, when placed in a @Configuration class, enable some basic functionality needed by a state machine.




    - @EnableStateMachine
    - here it extends EnumStateMachineConfigurerAdapter<States, Events> - two generic types for States and Events
    - Or just extends StateMachineConfigurerAdapter<S,E>
        - we perform three types of configurations
            - StateMachineConfigurationConfigurer - config for the state machine itself
                - here we config `autoStartup(true)` and `listener()`
            - StateMachineStateConfigurer - config for the states in the machine
                - we config the initial state and the states in the machine
            - StateMachineTransitionConfigurer - config for the transitions in the machine
                - we config the internal/external transitions from source state to the target state with event/time trigger

```
@Configuration
@EnableStateMachine
public class StateMachineConfig
        extends EnumStateMachineConfigurerAdapter<States, Events> {

    @Override
    public void configure(StateMachineConfigurationConfigurer<States, Events> config)
            throws Exception {
        config
            .withConfiguration()
                .autoStartup(true)
                .listener(listener());
    }

    @Override
    public void configure(StateMachineStateConfigurer<States, Events> states)
            throws Exception {
        states
            .withStates()
                .initial(States.SI)
                    .states(EnumSet.allOf(States.class));
    }

    @Override
    public void configure(StateMachineTransitionConfigurer<States, Events> transitions)
            throws Exception {
        transitions
            .withExternal()
                .source(States.SI).target(States.S1).event(Events.E1)
                .and()
            .withExternal()
                .source(States.S1).target(States.S2).event(Events.E2);
    }

    @Bean
    public StateMachineListener<States, Events> listener() {
        return new StateMachineListenerAdapter<States, Events>() {
            @Override
            public void stateChanged(State<States, Events> from, State<States, Events> to) {
                System.out.println("State change to " + to.getId());
            }
        };
    }
}
```

- Autowire StateMachine and CommandLineRunner run() method

```
@SpringBootApplication
    public class Application implements CommandLineRunner {

        public static void main(String[] args) {
            SpringApplication.run(Application.class, args);
        }

        @Autowired
        private StateMachine<States, Events> stateMachine;

        @Override
        public void run(String... args) throws Exception {
            stateMachine.sendEvent(Events.E1);
            stateMachine.sendEvent(Events.E2);
        }
}
```

- Actions 

    ```
    @Configuration
    @EnableStateMachine
    public class Config52
            extends EnumStateMachineConfigurerAdapter<States, Events> {

        @Override
        public void configure(StateMachineStateConfigurer<States, Events> states)
                throws Exception {
            states
                .withStates()
                    .initial(States.S1, action())
                    .state(States.S1, action(), null)
                    .state(States.S2, null, action())
                    .state(States.S2, action())
                    .state(States.S3, action(), action());
        }

        @Bean
        public Action<States, Events> action() {
            return new Action<States, Events>() {

                @Override
                public void execute(StateContext<States, Events> context) {
                    // do something
                }
            };
        }

    }
    ``` 

    - initial(States.S1, action())  : an action for the `initial state`, S1.
    - state(States.S3, action(), action()) : defined both `entry` and `exit` actions for state S3.
    - state S1 is used twice with initial() and state() functions
        - initial() function only runs a particular action when a state machine is started.
            - This action is an initializing action that is **run only once**. 
        - An action defined with state() is then run if the state machine **transitions back and forward between initial and non-initial states**.
    - **State actions are run differently compared to entry and exit actions**:
        - The execution happens after state has been entered 
        - The action **can be cancelled** if state exit happens before a particular action has been completed.
            - State actions are run by using a normal `Spring TaskScheduler` wrapped within a Runnable that can get cancelled through `ScheduledFuture`. 
            - **This means you need to catch InterruptedException or periodically check whether the thread is interrupted**.
        - The cancel policy for the actions can be set throuth StateMachineConfigurationConfigurer:
            - `IMMEDIATE_CANCEL`: immediately cancel a running task when its state is complete
            - `TIMEOUT_CANCEL` together with a global timeout for each machine: to await action completion before cancelation is requested. 

        ```
        @Override
        public void configure(StateMachineConfigurationConfigurer<String, String> config) throws Exception {
            config
                .withConfiguration()
                    .stateDoActionPolicy(StateDoActionPolicy.IMMEDIATE_CANCEL);
        }
        ```

        ```
        @Override
        public void configure(StateMachineConfigurationConfigurer<String, String> config) throws Exception {
            config
                .withConfiguration()
                    .stateDoActionPolicy(StateDoActionPolicy.TIMEOUT_CANCEL)
                    .stateDoActionPolicyTimeout(10, TimeUnit.SECONDS);
        }
        ```

        - If Event directly takes a machine into a state so that `event headers` are available to a particular action, you can also **use a dedicated event header to set a specific timeout (defined in millis).** You can use the reserved header value `StateMachineMessageHeaders.HEADER_DO_ACTION_TIMEOUT` for this purpose.

        ```
        @Autowired
        StateMachine<String, String> stateMachine;

        void sendEventUsingTimeout() {
            stateMachine.sendEvent(MessageBuilder
                    .withPayload("E1")
                    .setHeader(StateMachineMessageHeaders.HEADER_DO_ACTION_TIMEOUT, 5000)
                    .build());

        }
        ```

    - Transition Action Error Handling
        - You can always catch exceptions manually. However, with actions defined for **transitions**, you can define an error action that is called if an exception is raised. 
        - The exception is then available from a `StateContext` passed to that action. The following example shows how to create a state that handles an exception:

        ```

        @Override
        public void configure(StateMachineTransitionConfigurer<States, Events> transitions)
                throws Exception {
            transitions
                .withExternal()
                    .source(States.S1)
                    .target(States.S2)
                    .event(Events.E1)
                    .action(action(), errorAction()); //actions for this transition(1st: normal action, 2nd: error action)
        }

        @Bean
        public Action<States, Events> errorAction() {
            return new Action<States, Events>() {

                @Override
                public void execute(StateContext<States, Events> context) {
                    // RuntimeException("MyError") added to context
                    Exception exception = context.getException();
                    exception.getMessage();
                }
            };
        }
        ```




- Event Triggers
    - Currently supported triggers are EventTrigger and TimerTrigger.
    - EventTrigger : it allows user to directly interact with a state machine by sending events to it. 
        - Trigger is added to a transition simply by associating a state to it during a configuration.

        ```
        @Autowired 
        StateMachine<States, Events> stateMachine;

        void signalMachine() {
            stateMachine.sendEvent(Events.E1);

            Message<Events> message = MessageBuilder
                    .withPayload(Events.E2)
                    .setHeader("foo", "bar")
                    .build();
            stateMachine.sendEvent(message);
        }
        ```

        - We send an event using two different ways. 
            - Firstly we simply sent a type safe event using state machine api method `sendEvent(E event)`. 
            - Secondly we send event wrapped in a `Spring messaging Message` using api method `sendEvent(Message<E> message)` with a custom **event headers**. 
                - This allows user to add arbitrary extra information with an event which is then visible to StateContext when for example user is implementing actions.

[Note]
Message headers are generally passed on until machine runs to completion for a specific event. For example if an event is causing transition into a state A which have an anonymous transition into a state B, original event is available for actions or guards in state B.


- Use StateMachineFactory to create StateMachine

```
public class Bean3 {

	@Autowired
	StateMachineFactory<States, Events> factory;

	void method() {
		StateMachine<States,Events> stateMachine = factory.getStateMachine();
		stateMachine.start();
	}
}
```

- Event trigger

```
@Autowired
StateMachine<States, Events> stateMachine;

void signalMachine() {
	stateMachine.sendEvent(Events.E1);

	Message<Events> message = MessageBuilder
			.withPayload(Events.E2)
			.setHeader("foo", "bar")
			.build();
	stateMachine.sendEvent(message);
}
```

- timer action
    - PlayingAction updates an extended state variable called ELAPSEDTIME, which the player can use to read and update its LCD status display. PlayingAction also handles track shifting when the user goe back or forward in tracks. The following example defines PlayingAction:
```
public static class PlayingAction implements Action<States, Events> {

	@Override
	public void execute(StateContext<States, Events> context) {
		Map<Object, Object> variables = context.getExtendedState().getVariables();
		Object elapsed = variables.get(Variables.ELAPSEDTIME);
		Object cd = variables.get(Variables.CD);
		Object track = variables.get(Variables.TRACK);
		if (elapsed instanceof Long) {
			long e = ((Long)elapsed) + 1000l;
			if (e > ((Cd) cd).getTracks()[((Integer) track)].getLength()*1000) {
				context.getStateMachine().sendEvent(MessageBuilder
						.withPayload(Events.FORWARD)
						.setHeader(Headers.TRACKSHIFT.toString(), 1).build());
			} else {
				variables.put(Variables.ELAPSEDTIME, e);
			}
		}
	}
}
```