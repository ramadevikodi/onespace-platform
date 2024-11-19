BEGIN;
DO $$ BEGIN IF NOT EXISTS (
  SELECT
  FROM
    information_schema.tables
  WHERE
    table_name = 'application'
) THEN create table application (
      application_id         UUID    default gen_random_uuid() primary key,
      application_name       VARCHAR(256),
      short_description      text,
      long_description       text,
      url                    VARCHAR(256),
      registered_by          VARCHAR(120),
      registered_datetime    timestamp with time zone,
      icon                   VARCHAR(256),
      banner                 VARCHAR(256),
      version                VARCHAR(50),
      owner_organization     UUID references business_unit (business_unit_id),
      approver               VARCHAR(256),
      last_modified_datetime timestamp with time zone,
      published_datetime     timestamp with time zone,
      l1_comments            VARCHAR(512),
      l2_comments            VARCHAR(512),
      cost_center            VARCHAR(50),
      category_id            UUID references category (category_id),
      is_mfe                 boolean default false,
      is_sso_enabled         boolean default false,
      status_id              UUID references application_status (status_id),
      deployment_id          UUID references deployment (deployment_id),
      is_active              boolean default true,
      promote_as_upcoming_app boolean default false,
      promote_as_newapp      boolean default false,
      is_third_party         boolean default false
  );
RAISE NOTICE 'Table "application" created successfully.';
ELSE RAISE NOTICE 'Table "application" already exists.';
END IF;
EXCEPTION WHEN OTHERS THEN ROLLBACK;
RAISE EXCEPTION 'An error occurred during table creation. Transaction has been rolled back: %',
SQLERRM;
END $$;
COMMIT;
