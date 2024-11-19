BEGIN;
DO $$BEGIN
    INSERT INTO proposition (proposition_name, description)
    VALUES ('L0 Service Management pack',
            'Managed Technology Services is a long term strategic partnership contract that involves technology assessment and planning to identify and track the performance of healthcare technology over the term of the contract. This includes equipment replacements, upgrades, KPI selection, monitoring and reporting.'),
           ('Customer Support',
            'Philips Customer Service Portal is an online platform to manage Philips and multivendor fleet across modalities and departments. CSP can be used to manage fleet, see service status, identify systems that need attention, plan maintenance and request service and support.'),
           ('L1 Asset Management pack',
            'Managed Technology Services is a long term strategic partnership contract that involves technology assessment and planning to identify and track the performance of healthcare technology over the term of the contract. This includes equipment replacements, upgrades, KPI selection, monitoring and reporting.');

    RAISE NOTICE 'Data inserted successfully.';
EXCEPTION
    WHEN OTHERS THEN
        ROLLBACK;
        RAISE EXCEPTION 'An error occurred during the insertion. Transaction has been rolled back: %', SQLERRM;
END $$;
COMMIT;