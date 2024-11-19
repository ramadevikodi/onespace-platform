BEGIN;
DO $$
BEGIN

    update application set approver = 'platform.admin@philips.com' where application_name in (
'Customer Service Portal',
'Cyber Security',
'Utilization',
'Service Performance',
'Inventory',
'Assessment',
'Sentinel',
'HSP IAM Self Service',
'User Management',
'Dose Management'
);

update application set owner_organization  = '6c349e5b-c950-47e0-bb99-01b1b8922ad1' where application_name in (
'Customer Service Portal',
'Cyber Security',
'Utilization',
'Service Performance',
'Inventory',
'Assessment',
'Sentinel',
'HSP IAM Self Service',
'User Management',
'Dose Management'
);

update application set url  = 'https://www.philips.co.uk/healthcare/product/HC895001/dosewise-portal' where application_name = 'Dose Management';

update application set status_id = ( SELECT status_id FROM application_status WHERE status_name = 'Approved by Market/Solution Owner')
where application_name = 'Dose Management';

update application set status_id = (SELECT status_id FROM application_status WHERE status_name = 'Approved by Market/Solution Owner')
where application_name = 'User Management';

INSERT INTO application (application_id, application_name, short_description, long_description, url, registered_by,
registered_datetime, icon, banner, version, owner_organization, approver, last_modified_datetime, published_datetime,
l1_comments, l2_comments, cost_center, category_id, is_mfe, is_sso_enabled, status_id, deployment_id, is_active,
promote_as_upcoming_app, promote_as_newapp, is_third_party
)
VALUES ('034216df-f156-4bbc-abf2-06613edf0105',
        'Philips Learning Center (Totara)',
        'The Philips Learning Center is a talent experience platform (LMS/LX).',
        'The Philips Learning Center is a talent experience platform (LMS/LX) where formalized classroom training, face-to-face managed virtual classrooms, eLearning, assignments, accreditation and the ability to track progress is conducted.',
        'https://www.learn.philips.com/login/index.php',
        'platform.admin@philips.com',
        '2024-01-01 09:00:00.000 +0530',
        'http://d1u65tovhg2ye9.cloudfront.net/icons/DoseManagement.svg',
        '',
        '1.0',
        '6c349e5b-c950-47e0-bb99-01b1b8922ad1',
        'platform.admin@philips.com',
        '2024-01-01 09:00:00.000 +0530',
        '2024-01-01 09:00:00.000 +0530',
        '',
        '',
        'IN5CHS0703',
        (SELECT category_id FROM category WHERE category_name = 'Admin App'),
        false,
        false,
        (SELECT status_id FROM application_status WHERE status_name = 'Approved by Market/Solution Owner'),
        (SELECT deployment_id FROM deployment WHERE deployment_mode = 'Multi-tenant'),
        true,
        true,
        false,
        false
        );

INSERT INTO application (application_id, application_name, short_description, long_description, url, registered_by,
registered_datetime, icon, banner, version, owner_organization, approver, last_modified_datetime, published_datetime,
l1_comments, l2_comments, cost_center, category_id, is_mfe, is_sso_enabled, status_id, deployment_id, is_active,
promote_as_upcoming_app, promote_as_newapp, is_third_party
)
VALUES ('8773c975-880a-45f0-b0a4-1e95240fb08d',
        'Performance Bridge',
        'This dashboard enables hospitals to focus on improving operational efficiency and reducing costs.',
        'Enables hospitals to focus on improving operational efficiency and reducing costs, while maintaining an emphasis on quality, performance and value.',
        'https://www.usa.philips.com/healthcare/services/performance-improvement/performancebridge',
        'platform.admin@philips.com',
        '2024-01-01 09:00:00.000 +0530',
        'http://d1u65tovhg2ye9.cloudfront.net/icons/DoseManagement.svg',
        '',
        '1.0',
        '6c349e5b-c950-47e0-bb99-01b1b8922ad1',
        'platform.admin@philips.com',
        '2024-01-01 09:00:00.000 +0530',
        '2024-01-01 09:00:00.000 +0530',
        '',
        '',
        'IN5CHS0703',
        (SELECT category_id FROM category WHERE category_name = 'Admin App'),
        false,
        false,
        (SELECT status_id FROM application_status WHERE status_name = 'Approved by Market/Solution Owner'),
        (SELECT deployment_id FROM deployment WHERE deployment_mode = 'Multi-tenant'),
        true,
        false,
        true,
        false
        );

INSERT INTO application (application_id, application_name, short_description, long_description, url, registered_by,
registered_datetime, icon, banner, version, owner_organization, approver, last_modified_datetime, published_datetime,
l1_comments, l2_comments, cost_center, category_id, is_mfe, is_sso_enabled, status_id, deployment_id, is_active,
promote_as_upcoming_app, promote_as_newapp, is_third_party
)
VALUES ('35458bd9-02fe-4cb7-8906-b2bb58bc40e0',
        'InCenter',
        'This dashboard provides instructions on servicing Philips Products used by FSE, RSE, BioMed engineers.',
        'A Portal used by FSE, RSE, BioMed engineers to find instructions on servicing Philips Products and training videos.',
        'https://incenter.medical.philips.com/',
        'platform.admin@philips.com',
        '2024-01-01 09:00:00.000 +0530',
        'http://d1u65tovhg2ye9.cloudfront.net/icons/DoseManagement.svg',
        '',
        '1.0',
        '6c349e5b-c950-47e0-bb99-01b1b8922ad1',
        'platform.admin@philips.com',
        '2024-01-01 09:00:00.000 +0530',
        '2024-01-01 09:00:00.000 +0530',
        '',
        '',
        'IN5CHS0703',
        (SELECT category_id FROM category WHERE category_name = 'Admin App'),
        false,
        false,
        (SELECT status_id FROM application_status WHERE status_name = 'Approved by Market/Solution Owner'),
        (SELECT deployment_id FROM deployment WHERE deployment_mode = 'Multi-tenant'),
        true,
        false,
        true,
        false
        );

INSERT INTO application (application_id, application_name, short_description, long_description, url, registered_by,
registered_datetime, icon, banner, version, owner_organization, approver, last_modified_datetime, published_datetime,
l1_comments, l2_comments, cost_center, category_id, is_mfe, is_sso_enabled, status_id, deployment_id, is_active,
promote_as_upcoming_app, promote_as_newapp, is_third_party
)
VALUES ('f83a8925-f231-4782-a297-c5f61e261ba8',
        'Early Warning Scoring',
        'This dashboard provides insights on the automated early warning scoring and Rapid response to clinical deterioration.',
        'The indications of a patient’s clinical instability typically occur six to eight hours before an event.',
        'https://incenter.medical.philips.com/',
        'platform.admin@philips.com',
        '2024-01-01 09:00:00.000 +0530',
        'http://d1u65tovhg2ye9.cloudfront.net/icons/DoseManagement.svg',
        '',
        '1.0',
        '6c349e5b-c950-47e0-bb99-01b1b8922ad1',
        'platform.admin@philips.com',
        '2024-01-01 09:00:00.000 +0530',
        '2024-01-01 09:00:00.000 +0530',
        '',
        '',
        'IN5CHS0703',
        (SELECT category_id FROM category WHERE category_name = 'Admin App'),
        false,
        false,
        (SELECT status_id FROM application_status WHERE status_name = 'Approved by Market/Solution Owner'),
        (SELECT deployment_id FROM deployment WHERE deployment_mode = 'Multi-tenant'),
        true,
        false,
        true,
        false
        );

    RAISE NOTICE 'Data inserted successfully.';
EXCEPTION
    WHEN OTHERS THEN
        ROLLBACK;
        RAISE EXCEPTION 'An error occurred during the insertion. Transaction has been rolled back: %', SQLERRM;
END $$;
COMMIT;