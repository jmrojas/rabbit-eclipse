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
package rabbit.data.internal.xml.access;

import rabbit.data.access.model.TaskFileDataDescriptor;
import rabbit.data.internal.xml.DataStore;
import rabbit.data.internal.xml.DatatypeUtil;
import rabbit.data.internal.xml.merge.TaskFileEventTypeMerger;
import rabbit.data.internal.xml.schema.events.TaskFileEventListType;
import rabbit.data.internal.xml.schema.events.TaskFileEventType;
import rabbit.data.internal.xml.schema.events.TaskIdType;

import static org.junit.Assert.assertEquals;

import org.joda.time.LocalDate;

import java.util.List;

/**
 * @see TaskFileDataAccessor
 */
public class TaskFileDataAccessorTest extends
    AbstractNodeAccessorTest<TaskFileDataDescriptor, TaskFileEventType, TaskFileEventListType> {

  @Override
  protected TaskFileDataAccessor create() {
    return new TaskFileDataAccessor(
        DataStore.TASK_STORE, new TaskFileEventTypeMerger());
  }

  @Override
  public void testCreateDataNode() throws Exception {
    LocalDate eventDate = new LocalDate(2999, 9, 9);
    LocalDate creationDate = new LocalDate(3000, 1, 1);
    String handleId = "handleId";
    String fileId = "abcdefg";
    long duration = 9834;
    
    TaskIdType id = new TaskIdType();
    id.setCreationDate(DatatypeUtil.toXmlDate(creationDate));
    id.setHandleId(handleId);
    
    TaskFileEventType type = new TaskFileEventType();
    type.setDuration(duration);
    type.setFilePath(fileId);
    type.setTaskId(id);
    
    TaskFileDataDescriptor des = accessor.createDataNode(eventDate, type);
    assertEquals(duration, des.getDuration().getMillis());
    assertEquals(eventDate, des.getDate());
    assertEquals(fileId, des.getFilePath().toString());
    assertEquals(handleId, des.getTaskId().getHandleIdentifier());
    assertEquals(DatatypeUtil.toXmlDate(creationDate), type.getTaskId().getCreationDate());
  }

  @Override
  protected TaskFileEventListType createCategory() {
    TaskFileEventListType list = new TaskFileEventListType();
    list.setDate(DatatypeUtil.toXmlDate(new LocalDate()));
    return list;
  }

  @Override
  protected TaskFileEventType createElement() {
    TaskIdType id = new TaskIdType();
    id.setCreationDate(DatatypeUtil.toXmlDate(new LocalDate()));
    id.setHandleId("ok");
    
    TaskFileEventType type = new TaskFileEventType();
    type.setDuration(10);
    type.setFilePath("hello");
    type.setTaskId(id);
    return type;
  }

  @Override
  protected List<TaskFileEventType> getElements(TaskFileEventListType list) {
    return list.getTaskFileEvent();
  }

  @Override
  protected void setId(TaskFileEventType type, String id) {
    // Change one of the ID property - fileId or taskId
    type.setFilePath(id);
  }

  @Override
  protected void setValue(TaskFileEventType type, long value) {
    type.setDuration(value);
  }

  @Override
  protected boolean areEqual(TaskFileDataDescriptor expected,
      TaskFileDataDescriptor actual) {
    return expected.getDate().equals(actual.getDate())
        && expected.getDuration().equals(actual.getDuration())
        && expected.getFilePath().equals(actual.getFilePath())
        && expected.getTaskId().equals(actual.getTaskId());
  }
}