-----------------------------CREATION-----------------------------------------


create
extension pgcrypto;

create table deployment
(
    deployment_id   UUID default gen_random_uuid() primary key,
    deployment_mode VARCHAR(50)
);

create table speciality
(
    speciality_id   UUID default gen_random_uuid() primary key,
    speciality_name VARCHAR(256)
);

create table modality
(
    modality_id   UUID default gen_random_uuid() primary key,
    modality_name VARCHAR(256)
);

create table category
(
    category_id   UUID default gen_random_uuid() primary key,
    category_name VARCHAR(256),
    description   text
);

create table market
(
    market_id   uuid    default gen_random_uuid() primary key,
    market_name varchar(256),
    region      varchar(256),
    country     varchar(256),
    sub_region  varchar(256),
    is_active   boolean default true
);

create table application_status
(
    status_id   UUID default gen_random_uuid() primary key,
    status_name VARCHAR(50)
);

create table language
(
    language_id     UUID default gen_random_uuid() primary key,
    language_code   VARCHAR(256),
    description     VARCHAR(256),
    date_format     VARCHAR(256),
    time_format     VARCHAR(256),
    currency_format VARCHAR(256)
);

create table proposition
(
    proposition_id   UUID    default gen_random_uuid() primary key,
    proposition_name VARCHAR(256) unique,
    description      VARCHAR(512)
        constraint proposition_name_not_null check (
            not (
                proposition_name is null
                    or proposition_name = ''
                )
            ),
    is_active        boolean default true
);

create table customer
(
    customer_id   UUID    default gen_random_uuid() primary key,
    customer_name VARCHAR(256),
    description   VARCHAR(256),
    address       VARCHAR(256),
    email_contact VARCHAR(256),
    is_active     boolean default true,
    market_id     UUID references market (market_id)
);

create table customer_external_system_mapping
(
    customer_id    UUID references customer (customer_id) primary key,
    hsp_iam_org_id uuid,
    sap_account_id VARCHAR(256)
);

create table customer_location
(
    customer_id UUID references customer (customer_id),
    location    VARCHAR(256),
    site        VARCHAR(256),
    department  VARCHAR(256)
);

create table business_unit
(
    business_unit_id   UUID default gen_random_uuid() primary key,
    cluster            VARCHAR(256),
    segment            VARCHAR(256),
    business_unit_name VARCHAR(256),
    description        VARCHAR(512)
);

create table business_unit_external_system_mapping
(
    business_unit_id UUID references business_unit (business_unit_id) primary key,
    hsp_iam_org_id   uuid,
    sap_account_id   VARCHAR(256)
);

create table application
(
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

create table application_proposition_mapping
(
    application_id UUID references application (application_id),
    proposition_id UUID references proposition (proposition_id),
    primary key (application_id,
                 proposition_id)
);

create table application_speciality_mapping
(
    application_id UUID references application (application_id),
    speciality_id  UUID references speciality (speciality_id),
    primary key (application_id,
                 speciality_id)
);

create table application_language_mapping
(
    application_id UUID references application (application_id),
    language_id    UUID references language (language_id),
    primary key (application_id,
                 language_id)
);

create table application_modality_mapping
(
    application_id UUID references application (application_id),
    modality_id    UUID references modality (modality_id),
    primary key (application_id,
                 modality_id)
);

create table application_market_mapping
(
    application_id UUID references application (application_id),
    market_id      UUID references market (market_id),
    primary key (application_id,
                 market_id)
);

create table user_market_mapping
(
    hsp_user_uuid UUID,
    market_id     UUID references market (market_id),
    primary key (hsp_user_uuid,
                 market_id)
);

create table translations
(
    translation_id  UUID default gen_random_uuid() primary key,
    table_name      VARCHAR(256) not null,
    column_name     VARCHAR(256) not null,
    record_id       UUID         not null,
    language_code   VARCHAR(256) not null,
    translated_text text         not null,
    unique (table_name,
            column_name,
            record_id,
            language_code)
);

CREATE
OR REPLACE FUNCTION get_application_translations(
	record_id UUID,
    language_code VARCHAR(256)
)
RETURNS TABLE(
	column_name VARCHAR(256),
    description TEXT
) AS $$
BEGIN
RETURN QUERY EXECUTE format(
        'SELECT t.column_name, COALESCE(t.translated_text, a.short_description) AS description
         FROM application a
         LEFT JOIN translations t ON t.record_id = %L
                                  AND LOWER(t.language_code) = LOWER(%L)
		where a.application_id = record_id',
        record_id, language_code
    );
END;
$$
LANGUAGE plpgsql;

CREATE INDEX idx_translations_table_column_record_language
    ON translations (table_name, column_name, record_id, language_code);

CREATE OR REPLACE FUNCTION get_filter_criteria_for_app_integrator()
RETURNS TABLE (
    key TEXT,
    value TEXT[]
) AS $$
BEGIN
    RETURN QUERY
    SELECT DISTINCT
        'deployment' AS key,
        ARRAY_AGG(DISTINCT deployment_mode::TEXT) AS value
    FROM deployment
    GROUP BY key
    UNION ALL
    SELECT DISTINCT
        'market' AS key,
        ARRAY_AGG(DISTINCT market_name::TEXT) AS value
    FROM market m, application_market_mapping amm
	where amm.market_id = m.market_id
    GROUP BY key
    UNION ALL
    SELECT DISTINCT
        'region' AS key,
        ARRAY_AGG(DISTINCT region::TEXT) AS value
    FROM market m, application_market_mapping amm
	where amm.market_id = m.market_id
    GROUP BY key
    UNION ALL
    SELECT DISTINCT
        'country' AS key,
        ARRAY_AGG(DISTINCT country::TEXT) AS value
    FROM market m, application_market_mapping amm
	where amm.market_id = m.market_id
    GROUP BY key
    UNION ALL
    SELECT DISTINCT
        'sub_region'AS key,
        ARRAY_AGG(DISTINCT sub_region::TEXT) AS value
    FROM market m, application_market_mapping amm
	where amm.market_id = m.market_id
    GROUP BY key
    UNION ALL
    SELECT DISTINCT
        'language'AS key,
        ARRAY_AGG(DISTINCT description::TEXT) AS value
    FROM language l, application_language_mapping alm
	where alm.language_id = l.language_id
    GROUP BY key
	UNION ALL
	SELECT DISTINCT
        'sso_enabled' AS key,
        ARRAY_AGG(DISTINCT is_sso_enabled::TEXT) AS value
    FROM application
    GROUP BY key;
END;
$$ LANGUAGE plpgsql;

CREATE OR REPLACE FUNCTION get_filter_criteria_for_kam()
RETURNS TABLE (
    key TEXT,
    value TEXT[]
) AS $$
BEGIN
    RETURN QUERY
    SELECT DISTINCT
        'speciality' AS key,
        ARRAY_AGG(DISTINCT speciality_name::TEXT) AS value
    FROM speciality s, application_speciality_mapping asm
	where asm.speciality_id = s.speciality_id
    GROUP BY key
    UNION ALL
    SELECT DISTINCT
        'modality' AS key,
        ARRAY_AGG(DISTINCT modality_name::TEXT) AS value
    FROM modality m, application_modality_mapping amm
	where amm.modality_id = m.modality_id
    GROUP BY key
	UNION ALL
	SELECT DISTINCT
        'category' AS key,
        ARRAY_AGG(DISTINCT category_name::TEXT) AS value
    FROM category
    GROUP BY key
    UNION ALL
    SELECT DISTINCT
        'sub_region'AS key,
        ARRAY_AGG(DISTINCT sub_region::TEXT) AS value
    FROM market m, application_market_mapping amm
	where amm.market_id = m.market_id
    GROUP BY key
    UNION ALL
    SELECT DISTINCT
        'language'AS key,
        ARRAY_AGG(DISTINCT description::TEXT) AS value
    FROM language l, application_language_mapping alm
	where alm.language_id = l.language_id
    GROUP BY key;
END;
$$ LANGUAGE plpgsql;

CREATE OR REPLACE FUNCTION get_filter_criteria_for_hospital_end_user()
RETURNS TABLE (
    key TEXT,
    value TEXT[]
) AS $$
BEGIN
    RETURN QUERY
    SELECT DISTINCT
        'speciality' AS key,
        ARRAY_AGG(DISTINCT speciality_name::TEXT) AS value
    FROM speciality s, application_speciality_mapping asm
	where asm.speciality_id = s.speciality_id
    GROUP BY key
    UNION ALL
    SELECT DISTINCT
        'modality' AS key,
        ARRAY_AGG(DISTINCT modality_name::TEXT) AS value
    FROM modality m, application_modality_mapping amm
	where amm.modality_id = m.modality_id
    GROUP BY key
	UNION ALL
	SELECT DISTINCT
        'category' AS key,
        ARRAY_AGG(DISTINCT category_name::TEXT) AS value
    FROM category
    GROUP BY key
    UNION ALL
    SELECT DISTINCT
        'language'AS key,
        ARRAY_AGG(DISTINCT description::TEXT) AS value
    FROM language l, application_language_mapping alm
	where alm.language_id = l.language_id
    GROUP BY key;
END;
$$ LANGUAGE plpgsql;

--App notification tables
create table app_notification
(
    id UUID default gen_random_uuid() primary key,
    title character varying(256),
    message text,
    category character varying(256),
    source UUID references application (application_id),
    created_at timestamp with time zone,
    expiry timestamp with time zone
);

CREATE TABLE notification_recipient (
	id uuid,
	notification_id uuid references app_notification (id),
	recipient_id uuid,
	"read" bool
);

--App notification function to notify app_notifications channel
CREATE OR REPLACE FUNCTION notify_event_to_channel()
RETURNS TRIGGER AS $$
begin
	PERFORM pg_notify('app_notifications', NEW.id::text);
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

--App notification trigger to notify app_notifications channel
CREATE OR REPLACE TRIGGER appnotification_trigger
AFTER INSERT OR UPDATE ON app_notification
FOR EACH ROW
EXECUTE FUNCTION notify_event_to_channel();

CREATE OR REPLACE FUNCTION get_filter_criteria_for_platform_admin()
RETURNS TABLE (
    key TEXT,
    value TEXT[]
) AS $$
BEGIN
    RETURN QUERY
    SELECT DISTINCT
        'deployment' AS key,
        ARRAY_AGG(DISTINCT deployment_mode::TEXT) AS value
    FROM deployment
    GROUP BY key
    UNION ALL
    SELECT DISTINCT
        'market' AS key,
        ARRAY_AGG(DISTINCT market_name::TEXT) AS value
    FROM market
    GROUP BY key
    UNION ALL
    SELECT DISTINCT
        'region' AS key,
        ARRAY_AGG(DISTINCT region::TEXT) AS value
    FROM market
    GROUP BY key
    UNION ALL
    SELECT DISTINCT
        'country' AS key,
        ARRAY_AGG(DISTINCT country::TEXT) AS value
    FROM market
    GROUP BY key
    UNION ALL
    SELECT DISTINCT
        'sub_region'AS key,
        ARRAY_AGG(DISTINCT sub_region::TEXT) AS value
    FROM market
    GROUP BY key
    UNION ALL
    SELECT DISTINCT
        'language'AS key,
        ARRAY_AGG(DISTINCT description::TEXT) AS value
    FROM language
    GROUP BY key
	UNION ALL
	SELECT DISTINCT
        'sso_enabled' AS key,
        ARRAY_AGG(DISTINCT is_sso_enabled::TEXT) AS value
    FROM application
    GROUP BY key
	UNION ALL
	 SELECT DISTINCT
        'speciality' AS key,
        ARRAY_AGG(DISTINCT speciality_name::TEXT) AS value
    FROM speciality
    GROUP BY key
    UNION ALL
    SELECT DISTINCT
        'modality' AS key,
        ARRAY_AGG(DISTINCT modality_name::TEXT) AS value
    FROM modality
    GROUP BY key
	UNION ALL
	SELECT DISTINCT
        'category' AS key,
        ARRAY_AGG(DISTINCT category_name::TEXT) AS value
    FROM category
    GROUP BY key
	UNION ALL
	SELECT DISTINCT
        'statusName' AS key,
        ARRAY_AGG(DISTINCT status_name::TEXT) AS value
    FROM application_status
    GROUP BY key;
END;
$$ LANGUAGE plpgsql;

create table action
(
	action_id uuid default gen_random_uuid() primary key,
	title varchar(256),
	initiator uuid,
	datetime timestamp with time zone,
	type varchar(20),
	notify BOOLEAN,
	message varchar(2048),
	source UUID references application (application_id),
	due_datetime timestamp with time zone,
	expiry_datetime timestamp with time zone,
	metadata jsonb,
	related_object varchar(256)
);

create table action_owners
(
	action_owner_id uuid default gen_random_uuid() primary key,
	action_id uuid references action (action_id),
	potential_owner uuid,
	status varchar(20),
	completed_at_datetime timestamp with time zone
);
----------------------------------------------------------------------------
