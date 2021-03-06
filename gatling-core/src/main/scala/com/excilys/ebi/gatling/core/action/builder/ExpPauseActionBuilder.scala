/**
 * Copyright 2011-2012 eBusiness Information, Groupe Excilys (www.excilys.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * 		http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.excilys.ebi.gatling.core.action.builder

import java.util.concurrent.TimeUnit

import com.excilys.ebi.gatling.core.action.{ system, PauseAction }
import com.excilys.ebi.gatling.core.config.ProtocolConfigurationRegistry
import com.excilys.ebi.gatling.core.util.NumberHelper.createExpRandomLongGenerator

import akka.actor.{ Props, ActorRef }

object ExpPauseActionBuilder {

	/**
	 * Creates an initialized ExpPauseActionBuilder with a 1 second delay and a
	 * time unit in Seconds.  A 1 second delay is used because exponential distributions
	 * are not defined at zero and 1 is the smallest positive Long.
	 */
	def expPauseActionBuilder = new ExpPauseActionBuilder(1, TimeUnit.SECONDS, null)
}

/**
 * Builder for the 'pauseExp' action.  Creates PauseActions for a user with a delay coming from
 * an exponential distribution with the specified mean duration.
 *
 * @constructor create a new ExpPauseActionBuilder
 * @param meanDuration mean duration of the generated pause
 * @param timeUnit time unit of the duration of the generated pause
 * @param next action that will be executed after the generated pause
 */
class ExpPauseActionBuilder(meanDuration: Long, timeUnit: TimeUnit, next: ActorRef) extends ActionBuilder {

	/**
	 * Adds meanDuration to builder
	 *
	 * @param meanDuration the minimum duration of the pause
	 * @return a new builder with minDuration set
	 */
	def withMeanDuration(meanDuration: Long) = new ExpPauseActionBuilder(meanDuration, timeUnit, next)

	/**
	 * Adds timeUnit to builder
	 *
	 * @param timeUnit time unit of the duration
	 * @return a new builder with timeUnit set
	 */
	def withTimeUnit(timeUnit: TimeUnit) = new ExpPauseActionBuilder(meanDuration, timeUnit, next)

	def withNext(next: ActorRef) = new ExpPauseActionBuilder(meanDuration, timeUnit, next)

	def build(protocolConfigurationRegistry: ProtocolConfigurationRegistry) = {
		val meanDurationInMillis = TimeUnit.MILLISECONDS.convert(meanDuration, timeUnit)
		val delayGenerator: () => Long = createExpRandomLongGenerator(meanDurationInMillis)

		system.actorOf(Props(new PauseAction(next, delayGenerator)))
	}
}
