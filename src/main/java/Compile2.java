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
