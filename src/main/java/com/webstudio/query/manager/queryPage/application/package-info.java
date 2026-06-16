/**
 * Use case for executing paged queries.
 *
 * <p>
 * {@link com.webstudio.query.manager.queryPage.application.QueryPageUseCaseInteractor}
 * validates a client query (including page index and size bounds), maps it to
 * the repository field model, delegates to a
 * {@link com.webstudio.query.manager.queryPage.application.port.QueryPageRepository},
 * and returns a {@link com.webstudio.query.manager.queryPage.application.Page}
 * of rows mapped to the output field model.
 */
package com.webstudio.query.manager.queryPage.application;
