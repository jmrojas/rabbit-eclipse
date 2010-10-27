/*
 * Copyright 2010 The Rabbit Eclipse Plug-in Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package rabbit.ui.internal.pages.java;

import rabbit.ui.internal.pages.java.JavaCategory;

import static org.junit.Assert.assertNotNull;

import org.junit.Test;

import java.lang.reflect.Field;

/**
 * @see JavaCategory
 */
public class JavaCategoryTest {

  @Test
  public void testCatergories() throws Exception {
    Field[] fields = JavaCategory.class.getDeclaredFields();
    for (Field field : fields) {
      if (field.isAccessible()) {
        JavaCategory category = (JavaCategory) field.get(null);
        assertNotNull(category.getText());
        assertNotNull(category.getImageDescriptor());
      }
    }
  }
}