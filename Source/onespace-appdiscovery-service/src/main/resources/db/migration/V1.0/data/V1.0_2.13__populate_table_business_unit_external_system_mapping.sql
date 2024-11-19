BEGIN;
DO $$
BEGIN
    INSERT INTO business_unit_external_system_mapping (business_unit_id, hsp_iam_org_id, sap_account_id)
    VALUES ('6c349e5b-c950-47e0-bb99-01b1b8922ad1', '9d59813a-ee23-47db-a7f5-42f6f7689319', 'BU0001'),
           ('a5808bc0-91cc-40d3-b5f4-159075bef93b', 'e15f04ea-3511-4116-a18d-9670f6b9b6a7', 'BU0002'),
           ('a5808bc0-91cc-40d3-b5f4-169075bef93b', 'fde39cae-a6bb-440a-a387-8b786f356179', 'BU0003'),
           ('a5808bc0-91cc-40d3-b5f4-179075bef93b', 'ca18d1f6-8797-4eae-8544-b8f9eaad55ad', 'BU0004'),
           ('a5808bc0-91cc-40d3-b5f4-189075bef93b', 'b319af3c-ee58-47a4-b777-9220452ec75b', 'BU0005'),
           ('a5808bc0-91cc-40d3-b5f4-199075bef93b', '655441d5-b45c-4b55-9cbb-b60f4e20d085', 'BU0006'),
           ('a5808bc0-91cc-40d3-b5f4-209075bef93b', 'f6b87b67-2837-4e8a-be82-2fa0855cb021', 'BU0007'),
           ('a5808bc0-91cc-40d3-b5f4-219075bef93b', '1ff6624d-dc06-4a27-b7ab-18407f443cb7', 'BU0008'),
           ('a5808bc0-91cc-40d3-b5f4-229075bef93b', '34c45882-7908-4a8a-a9ae-25d41b8a0ab8', 'BU0009'),
           ('a5808bc0-91cc-40d3-b5f4-239075bef93b', 'ae785083-e05c-4853-95f0-33f274dd9126', 'BU00010'),
           ('a5808bc0-91cc-40d3-b5f4-249075bef93b', 'ce33028f-69c3-42c9-a3b1-c6b33a12bef9', 'BU00011'),
           ('a5808bc0-91cc-40d3-b5f4-259075bef93b', '31a7970f-3749-4579-8123-1a805add32e4', 'BU00012'),
           ('a5808bc0-91cc-40d3-b5f4-269075bef93b', '0be6aaca-ea38-41c6-a2a4-84636231b636', 'BU00013'),
           ('a5808bc0-91cc-40d3-b5f4-279075bef93b', 'd55c5acc-c483-4910-b721-5fbbfbe284a6', 'BU00014'),
           ('a5808bc0-91cc-40d3-b5f4-289075bef93b', '207911cd-a288-41ca-94bb-cc329d76908b', 'BU00015'),
           ('a5808bc0-91cc-40d3-b5f4-299075bef93b', '737a9b59-446b-44fb-9fa8-ee2db9304e6f', 'BU00016'),
           ('a5808bc0-91cc-40d3-b5f4-309075bef93b', 'e1e33309-81ee-4d9b-b7ef-9dd0813f2c7b', 'BU00017');


     RAISE NOTICE 'Data inserted successfully.';
EXCEPTION
    WHEN OTHERS THEN        
        ROLLBACK;        
        RAISE EXCEPTION 'An error occurred during the insertion. Transaction has been rolled back: %', SQLERRM;
END $$;
COMMIT;