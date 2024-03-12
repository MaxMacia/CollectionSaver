-- Insert roles
INSERT INTO roles(name) VALUES('ROLE_USER');
INSERT INTO roles(name) VALUES('ROLE_ADMIN');

-- Insert a users
INSERT INTO public.users(
	id, email, "password", username)
	VALUES (1, 'max@mail.com', '$2a$10$XP2JmpG82OA5HpgYSgUsteo6U3tRmkgAfeEs13C0PD6FC4oVvg1Wa', 'max');

-- Assign roles to the created persons
INSERT INTO public.users_roles(
	user_id, role_id)
	VALUES (1, 2);