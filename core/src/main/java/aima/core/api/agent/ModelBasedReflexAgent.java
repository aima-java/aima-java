package aima.core.api.agent;

import java.util.Optional;
import java.util.Set;

/**
 * Artificial Intelligence A Modern Approach (4th Edition): Figure ??, page ??.<br>
 * <br>
 *
 * <pre>
 * function MODEL-BASED-REFLEX-AGENT(percept) returns an action
 *   persistent: state, the agent's current conception of the world state
 *               model, a description of how the next state depends on current state and action
 *               rules, a set of condition-action rules
 *               action, the most recent action, initially none
 *
 *   state  &lt;- UPDATE-STATE(state, action, percept, model)
 *   rule   &lt;- RULE-MATCH(state, rules)
 *   action &lt;- rule.ACTION
 *   return action
 * </pre>
 *
 * Figure ?? A model-based reflex agent. It keeps track of the current state
 * of the world using an internal model. It then chooses an action in the same
 * way as the reflex agent.
 *
 * @param <S> the type of internal state representation used by the agent.
 * @param <M> the type of internal model representation used by the agent.
 *
 * @author Ciaran O'Reilly
 */
public interface ModelBasedReflexAgent<A, P, S, M> extends Agent<A, P> {

    // persistent: state, the agent's current conception of the world state
    //             model, a description of how the next state depends on current state and action
    //             rules, a set of condition-action rules
    //             action, the most recent action, initially none
    S getState();
    void setState(S state);
    M model();
    Set<Rule<A, S>> rules();
    A getAction();
    void setAction(A action);

    // function MODEL-BASED-REFLEX-AGENT(percept) returns an action
    @Override
    default A perceive(P percept) {
        // state  <- UPDATE-STATE(state, action, percept, model)
        setState(updateState(getState(), getAction(), percept, model()));
        // rule   <- RULE-MATCH(state, rules)
        Optional<Rule<A, S>> rule = ruleMatch(getState(), rules());
        // action <- rule.ACTION
        setAction(rule.isPresent() ? rule.get().action() : null);
        // return action
        return getAction();
    }

    // state  <- UPDATE-STATE(state, action, percept, model)
    S updateState(S currentState, A mostRecentAction, P percept, M model);

    // rule <- RULE-MATCH(state, rules)
    default Optional<Rule<A, S>> ruleMatch(S state, Set<Rule<A, S>> rules) {
        return rules.stream().filter(rule -> rule.condition().test(state)).findFirst();
    }
}
