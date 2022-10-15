package com.example.sprintboot.statemachine_demo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.statemachine.StateContext;
import org.springframework.statemachine.action.Action;
import org.springframework.statemachine.config.EnableStateMachine;
import org.springframework.statemachine.config.EnumStateMachineConfigurerAdapter;
import org.springframework.statemachine.config.StateMachineConfigurerAdapter;
import org.springframework.statemachine.config.builders.StateMachineConfigurationConfigurer;
import org.springframework.statemachine.config.builders.StateMachineStateConfigurer;
import org.springframework.statemachine.config.builders.StateMachineTransitionConfigurer;

import java.util.logging.Logger;

@Configuration
@EnableStateMachine
public class SimpleEnumStateMachineConfiguration extends StateMachineConfigurerAdapter<ApplicationReviewStates, ApplicationReviewEvents> {
    private static final Logger LOGGER = Logger.getLogger(SimpleEnumStateMachineConfiguration.class.getName());
    @Override
    public void configure(StateMachineConfigurationConfigurer<ApplicationReviewStates, ApplicationReviewEvents> config)
            throws Exception {
        config
                .withConfiguration()
                .autoStartup(true)
                .beanFactory(null)
                .listener(new MyStateMachineListener());
    }

    @Override
    public void configure(StateMachineStateConfigurer<ApplicationReviewStates, ApplicationReviewEvents> states) throws Exception {
        states
                .withStates()
                .initial(ApplicationReviewStates.PEER_REVIEW)
                .state(ApplicationReviewStates.PRINCIPAL_REVIEW)
                .end(ApplicationReviewStates.APPROVED)
                .end(ApplicationReviewStates.REJECTED);

    }

    @Override
    public void configure(StateMachineTransitionConfigurer<ApplicationReviewStates, ApplicationReviewEvents> transitions) throws Exception {
        transitions.withExternal()
                .source(ApplicationReviewStates.PEER_REVIEW).target(ApplicationReviewStates.PRINCIPAL_REVIEW).action(timerAction()).timerOnce(12000)
                .and().withExternal()
                .source(ApplicationReviewStates.PRINCIPAL_REVIEW).target(ApplicationReviewStates.APPROVED).action(timerAction()).timerOnce(12000)
                .and().withExternal()
                .source(ApplicationReviewStates.PEER_REVIEW).target(ApplicationReviewStates.REJECTED).event(ApplicationReviewEvents.REJECT)
                .and().withExternal()
                .source(ApplicationReviewStates.PRINCIPAL_REVIEW).target(ApplicationReviewStates.REJECTED).event(ApplicationReviewEvents.REJECT);
    }

    //    @Override
//    public void configure(StateMachineTransitionConfigurer<ApplicationReviewStates, ApplicationReviewEvents> transitions) throws Exception {
//        transitions.withExternal()
//                .source(ApplicationReviewStates.PEER_REVIEW).target(ApplicationReviewStates.PRINCIPAL_REVIEW).event(ApplicationReviewEvents.APPROVE)
//                .and().withExternal()
//                .source(ApplicationReviewStates.PRINCIPAL_REVIEW).target(ApplicationReviewStates.APPROVED).event(ApplicationReviewEvents.APPROVE)
//                .and().withExternal()
//                .source(ApplicationReviewStates.PEER_REVIEW).target(ApplicationReviewStates.REJECTED).event(ApplicationReviewEvents.REJECT)
//                .and().withExternal()
//                .source(ApplicationReviewStates.PRINCIPAL_REVIEW).target(ApplicationReviewStates.REJECTED).event(ApplicationReviewEvents.REJECT);
//    }
    @Bean
    public TimerAction timerAction() {
        return new TimerAction();
    }

    public static class TimerAction implements Action<ApplicationReviewStates, ApplicationReviewEvents> {

        @Override
        public void execute(StateContext<ApplicationReviewStates, ApplicationReviewEvents> context) {
            LOGGER.info("TimerAction is called for the target:"+ context.getTarget()+", from the source:"+context.getSource());
        }
    }
}