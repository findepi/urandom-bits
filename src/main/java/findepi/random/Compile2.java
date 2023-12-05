/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package findepi.random;

@SuppressWarnings("unused")
public class Compile2 {

	public interface Entity<Id> {
	}

	public interface DAO<EntityType, Id> {
	}

	public <Id,
			EntityType extends Entity<Id>,
			Implementation extends DAO<EntityType, Id>>
			Implementation find(Class<EntityType> xClass) {

		// TODO Auto-generated method stub
		return null;
	}

	String texts[] = new String[] { "a", "b", "c", };

	static class FirstEntity implements Entity<String> {
	}

	static class FirstEntityDao implements DAO<FirstEntity, String> {
	}

	static class SecondEntity implements Entity<String> {
	}

	interface SecondEntityDao<EntityType extends SecondEntity> extends DAO<EntityType, String> {
	}

	private void doSomething() {
		// this works
		FirstEntityDao firstDao = find(FirstEntity.class);

		// this should work but does not
		SecondEntityDao<SecondEntity> secondDao = find(SecondEntity.class);

		// however this works
		DAO<SecondEntity, String> secondDao2 = find(SecondEntity.class);
	}
}
