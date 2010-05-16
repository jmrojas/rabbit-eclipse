/*
 * Copyright 2010 The Rabbit Eclipse Plug-in Project
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package rabbit.tracking.tests.trackers;

import rabbit.data.store.model.CommandEvent;
import rabbit.tracking.internal.trackers.CommandTracker;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.Command;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.IHandler;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.commands.ICommandService;
import org.eclipse.ui.handlers.IHandlerService;
import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;

import java.util.Collections;

/**
 * Test for {@link CommandTracker}
 */
@SuppressWarnings("restriction")
public class CommandTrackerTest extends AbstractTrackerTest<CommandEvent> {

  private CommandTracker tracker;

  @Before
  public void setUp() {
    tracker = createTracker();
  }

  @Test
  public void testDisabled() throws Exception {
    tracker.setEnabled(false);

    Command command = getCommandService().getCommand(
        System.currentTimeMillis() + "." + System.nanoTime());
    command.define("a", "b", getCommandService().getDefinedCategories()[0]);

    getHandlerService().activateHandler(command.getId(), createHandler());
    getHandlerService().executeCommand(command.getId(), null);

    assertTrue(tracker.getData().isEmpty());
  }

  @Test
  public void testExecution() throws Exception {
    tracker.setEnabled(true);

    String name = "cmdName";
    String description = "cmdDescription";
    Command command = getCommandService().getCommand(
        System.currentTimeMillis() + "." + System.nanoTime());
    command.define(name, description, getCommandService()
        .getDefinedCategories()[0]);

    long start = System.currentTimeMillis();
    getHandlerService().activateHandler(command.getId(), createHandler());
    getHandlerService().executeCommand(command.getId(), null);
    long end = System.currentTimeMillis();

    assertEquals(1, tracker.getData().size());
    CommandEvent e = tracker.getData().iterator().next();
    assertEquals(command, e.getExecutionEvent().getCommand());
    assertTrue(start <= e.getTime().getMillis());
    assertTrue(end >= e.getTime().getMillis());
  }

  @Override
  protected CommandEvent createEvent() {
    return new CommandEvent(new DateTime(), createExecutionEvent("1"));
  }

  @Override
  protected CommandTracker createTracker() {
    return new CommandTracker();
  }

  private ExecutionEvent createExecutionEvent(String commandId) {
    return new ExecutionEvent(getCommandService().getCommand(commandId),
        Collections.EMPTY_MAP, null, null);
  }

  private IHandler createHandler() {
    return new AbstractHandler() {
      @Override
      public Object execute(ExecutionEvent event) throws ExecutionException {
        return null;
      }
    };
  }

  private ICommandService getCommandService() {
    return (ICommandService) PlatformUI.getWorkbench().getService(
        ICommandService.class);
  }

  private IHandlerService getHandlerService() {
    return (IHandlerService) PlatformUI.getWorkbench().getService(
        IHandlerService.class);
  }
}
