BEGIN;
DO $$
BEGIN    
    INSERT INTO application (application_id, application_name, short_description, long_description, url, registered_by,
    registered_datetime, icon, banner, version, owner_organization, approver, last_modified_datetime, published_datetime,
    l1_comments, l2_comments, cost_center, category_id, is_mfe, is_sso_enabled, status_id, deployment_id, is_active,
    promote_as_upcoming_app, promote_as_newapp, is_third_party
    )
    VALUES ('0d9972a1-5715-4f88-a3b5-e00826ae81b1',
            'Customer Service Portal',
            'The Philips Customer Services Portal is an online platform designed to provide customers with easy access to a range of support and service options for their Philips products.',
    		'This portal typically allows users to:
    		1. **Register Products**: Customers can register their Philips products to ensure they are eligible for warranties and receive important updates.
    		2. **Request Service**: Users can log service requests for product repairs, maintenance, and other technical support needs.
    		3. **Track Requests**: Customers can monitor the status of their service requests and get real-time updates on the progress of repairs or maintenance activities.
    		4. **Access Documentation**: The portal often provides access to user manuals, troubleshooting guides, and other important documentation related to Philips products.
    		5. **Order Parts and Accessories**: Customers can order replacement parts, accessories, and consumables for their Philips products.
    		6. **Contact Support**: The portal typically includes options for contacting customer support through various channels including email, chat, or phone.
    		7. **Training and Resources**: Some versions of the portal may offer training materials or resources to help customers get the most out of their Philips products.
    		Overall, the Philips Customer Services Portal is intended to streamline and enhance the customer service experience by providing a centralized, convenient, and user-friendly interface for managing all support-related needs.',
            'https://philipsb2bsc--4sdev.sandbox.my.site.com/Service/services/auth/sso/Open_ID_Connect_With_EPO',
            'platform.admin@philips.com',
    		'2024-01-01 09:00:00.000 +0530',
            'http://d1u65tovhg2ye9.cloudfront.net/icons/CustomerServicePortal.svg',
    		'',
            '1.0',
            '6c349e5b-c950-47e0-bb99-01b1b8922ad1',
            'platform administrator',
            '2024-01-01 09:00:00.000 +0530',
            '2024-01-01 09:00:00.000 +0530',
            '',
            '',
            'IN5CHS0703',
            (SELECT category_id FROM category WHERE category_name = 'IT App'),
    		false,
    		false,
            (SELECT status_id FROM application_status WHERE status_name = 'Approved by Market/Solution Owner'),
            (SELECT deployment_id FROM deployment WHERE deployment_mode = 'Multi-tenant'),
            true,
            false,
            false,
            false
            );

    INSERT INTO application (application_id, application_name, short_description, long_description, url, registered_by,
    registered_datetime, icon, banner, version, owner_organization, approver, last_modified_datetime, published_datetime,
    l1_comments, l2_comments, cost_center, category_id, is_mfe, is_sso_enabled, status_id, deployment_id, is_active,
    promote_as_upcoming_app, promote_as_newapp, is_third_party
    )
    VALUES ('f36347a4-b886-4c4e-ac5f-98443e7f299b',
            'Cyber Security',
            'This dashboard provides insights to manage the security posture & proactively address vulnerability of assets.',
    		'Increasingly rigorous security policies guide the needed development of the cybersecurity
    		policy and compliance. Healthcare Organizations need to work on standardization and
    		cross-integration of systems across vendors to effectively anticipate, assess and manage
    		new and emerging threats. Transparency in security KPIs is key in the review of the security
    		status to assess the risk for ransomware and security breaches to ensure optimum
    		performance and patient safety. The Cybersecurity Dashboard supports the review on
    		Philips’ response to security events and what areas need specific attention to minimize the
    		medical device vulnerabilities that can be exploited.
    		The Cybersecurity Dashboard is intended to be used by healthcare professionals to
    		evaluate key performance indicators (KPIs) using near real-time asset management data.
    		Key performance indicators (KPIs) are displayed utilizing near real-time logfile data,
    		depending on enabled PRS connectivity for your installed products.
    		The cloud-based Cybersecurity Dashboard is accessible 24x7 and assists in generating the
    		required reports when necessary.',
            'https://acc.insights.philips.com/sense/app/e7c47860-4b47-4bf6-a0b9-a0ad6a2b2496',
    		'platform.admin@philips.com',
    		'2024-01-01 09:00:00.000 +0530',
            'http://d1u65tovhg2ye9.cloudfront.net/icons/CyberSecurity.svg',
    		'',
            '1.0',
            '6c349e5b-c950-47e0-bb99-01b1b8922ad1',
            'platform administrator',
            '2024-01-01 09:00:00.000 +0530',
            '2024-01-01 09:00:00.000 +0530',
            '',
            '',
            'IN5CHS0735',
            (SELECT category_id FROM category WHERE category_name = 'IT App'),
    		false,
    		false,
            (SELECT status_id FROM application_status WHERE status_name = 'Approved by Market/Solution Owner'),
            (SELECT deployment_id FROM deployment WHERE deployment_mode = 'Multi-tenant'),
            true,
            false,
            false,
            false
            );

    INSERT INTO application (application_id, application_name, short_description, long_description, url, registered_by,
    registered_datetime, icon, banner, version, owner_organization, approver, last_modified_datetime, published_datetime,
    l1_comments, l2_comments, cost_center, category_id, is_mfe, is_sso_enabled, status_id, deployment_id, is_active,
    promote_as_upcoming_app, promote_as_newapp, is_third_party
    )
    VALUES ('27e2e700-0aa9-4198-a8e5-16aba4100335',
            'Utilization',
            'This dashboard provides insights about scanner & technologist’s utilization across departments.',
    		'Data-enhanced workflows are the cornerstone of quality care delivery for healthcare providers. Increasing operational effectiveness is key with the growing number of patients and imaging needs, combined with the need to reduce costs and increase efficiency. Whilst quality is the aim, accessibility to care is key. The capacity of imaging assets needs to be utilized in the best possible way to provide quality and accessible care. Utilization insights provide needed controls to create deepened understanding and actionable intelligence to drive operational optimization.
        	The dashboard enhances transparency in utilization measures between you and Philips, guarantees 99% dashboard uptime and assists in generating the required reports. In case of data discrepancy or data quality concerns, please contact your Philips Advisor. The Utilization Dashboard opens the needed conversation to continuously improve data quality available for your fleet and maintenance.',
            'https://acc.insights.philips.com/sense/app/9cd3af8d-bb37-4c92-8838-88a963eb270c',
            'platform.admin@philips.com',
    		'2024-01-01 09:00:00.000 +0530',
            'http://d1u65tovhg2ye9.cloudfront.net/icons/Utilization.svg',
    		'',
            '1.0',
            '6c349e5b-c950-47e0-bb99-01b1b8922ad1',
            'platform administrator',
            '2024-01-01 09:00:00.000 +0530',
            '2024-01-01 09:00:00.000 +0530',
            '',
            '',
            'IN5CHS0729',
            (SELECT category_id FROM category WHERE category_name = 'IT App'),
    		false,
    		false,
            (SELECT status_id FROM application_status WHERE status_name = 'Approved by Market/Solution Owner'),
            (SELECT deployment_id FROM deployment WHERE deployment_mode = 'Multi-tenant'),
            true,
            false,
            false,
            false
            );

    INSERT INTO application (application_id, application_name, short_description, long_description, url, registered_by,
    registered_datetime, icon, banner, version, owner_organization, approver, last_modified_datetime, published_datetime,
    l1_comments, l2_comments, cost_center, category_id, is_mfe, is_sso_enabled, status_id, deployment_id, is_active,
    promote_as_upcoming_app, promote_as_newapp, is_third_party
    )
    VALUES ('b66bf700-303e-4ed1-8dc6-16aa472c8861',
            'Service Performance',
            'This dashboard provides insights to understand the equipment up-time & service performance of the fleet.',
    		'Data-enhanced workflows are the cornerstone of quality care delivery for healthcare providers. Whilst quality is the aim, access to care is key. Keeping the large variety of medical technologies up and running from many different vendors is the challenge. Service Performance dashboard offers one entry point for all your Philips service contracts with uniform views per system types, locations, and departments to assess the current contracts and service performance, corrective - and planned maintenance and uptime status. For all assets or under Philips’ care, it provides the overview of the service performance to assess desired values and what is at risk of hampering performance and blocking accessibility and availability of medical technologies.
        	The dashboard enhances transparency in service performance measures between you and Philips, guarantees 99% dashboard uptime and assists in generating the required reports. In case of data discrepancy or data quality concerns, please contact your Philips Advisor. The Service Performance Dashboards opens the needed conversation to continuously improve data quality available for your fleet and maintenance.',
            'https://acc.insights.philips.com/sense/app/46d5d06c-ec37-4a1c-893a-f51f988f43b5',
    		'platform.admin@philips.com',
    		'2024-01-01 09:00:00.000 +0530',
            'http://d1u65tovhg2ye9.cloudfront.net/icons/ServicePerformance.svg',
    		'',
            '3.0.4',
            '6c349e5b-c950-47e0-bb99-01b1b8922ad1',
            'platform administrator',
            '2024-01-01 09:00:00.000 +0530',
            '2024-01-01 09:00:00.000 +0530',
            '',
            '',
            'IN5CHS0729',
            (SELECT category_id FROM category WHERE category_name = 'IT App'),
    		false,
    		false,
            (SELECT status_id FROM application_status WHERE status_name = 'Approved by Market/Solution Owner'),
            (SELECT deployment_id FROM deployment WHERE deployment_mode = 'Multi-tenant'),
            true,
            false,
            false,
            false
            );

    INSERT INTO application (application_id, application_name, short_description, long_description, url, registered_by,
    registered_datetime, icon, banner, version, owner_organization, approver, last_modified_datetime, published_datetime,
    l1_comments, l2_comments, cost_center, category_id, is_mfe, is_sso_enabled, status_id, deployment_id, is_active,
    promote_as_upcoming_app, promote_as_newapp, is_third_party
    )
    VALUES ('0be36caf-5b8d-4871-b91d-4ab9e7662f21',
            'Inventory',
            'This dashboard provides insights on equipment lifecycle and remote connectivity status.',
    		'In support of maximizing your technology investments, Philips aims to provide a single solution for
    		accessing insights and analytics across multiple sites, departments, installed products, and applications.
    		Healthcare organizations are increasingly focusing on data-driven approaches to improve both business and
    		patient outcomes. They need advanced insights, analytics, control, and support to immediately act and plan
    		on pressing matters to keep the healthcare organization running 24/7. Within a limited budget, they need
    		an easy-to-use experience that enables them to focus on things that matter.

    		The Inventory Dashboard is a cloud-based platform providing an accurate, near real-time visual overview of
    		all the connected assets. The cross department, vendor agnostics and accessible insights support CAPEX related decision making.

    		The Inventory Dashboard can be configured to allow navigation to the Inventory Assessment Dashboard.
    		Contact your Philips representative to understand the conditions to enable the Inventory Assessment
    		Dashboard for your healthcare organization.

    		Key elements in asset management and CAPEX planning is understanding the fleet of installed product and
    		maintaining current and accurate view for the healthcare organization, together with the set of different
    		manufacturers/OEMs; whether the installed product still has a service contract or warranty; whether the
    		installed product is still sending data to the manufacturer; if the installed product is considered end of life
    		or end of support; and how old the installed product is.

    		The Inventory Dashboard provides an overview of your installed product status to support data-driven
    		decision making to improve operational efficiency. It provides data transparency and visualizes a set of KPIs
    		that support in the indication where action (and potentially investment) is needed in the short-, mid- and
    		long-term.

    		The Inventory Dashboard is intended to be used by healthcare professionals to evaluate key performance
    		indicators (KPIs) using near real-time asset management data. Depending on enabled PRS connectivity for
    		your installed products, logfile based data is utilized for installed product intelligence metrics.
    		The cloud-based Inventory Dashboard is accessible 24x7 and assists in generating the required reports
    		when necessary.',
            'https://acc.insights.philips.com/sense/app/46d5d06c-ec37-4a1c-893a-f51f988f43b5',
    		'platform.admin@philips.com',
    		'2024-01-01 09:00:00.000 +0530',
            'http://d1u65tovhg2ye9.cloudfront.net/icons/Inventory.svg',
    		'',
            '3.0.4',
            '6c349e5b-c950-47e0-bb99-01b1b8922ad1',
            'platform administrator',
            '2024-01-01 09:00:00.000 +0530',
            '2024-01-01 09:00:00.000 +0530',
            '',
            '',
            'IN5CHS0729',
            (SELECT category_id FROM category WHERE category_name = 'IT App'),
    		false,
    		false,
            (SELECT status_id FROM application_status WHERE status_name = 'Approved by Market/Solution Owner'),
            (SELECT deployment_id FROM deployment WHERE deployment_mode = 'Multi-tenant'),
            true,
            false,
            false,
            false
            );


    INSERT INTO application (application_id, application_name, short_description, long_description, url, registered_by,
    registered_datetime, icon, banner, version, owner_organization, approver, last_modified_datetime, published_datetime,
    l1_comments, l2_comments, cost_center, category_id, is_mfe, is_sso_enabled, status_id, deployment_id, is_active,
    promote_as_upcoming_app, promote_as_newapp, is_third_party
    )
    VALUES ('ec477a16-671a-4e42-81a6-42eb6941c34a',
            'Assessment',
            'This dashboard provides insights on equipment status and prioritization for investment.',
    		'Healthcare organizations face financial challenges in planning for the acquisition and/or replacement of
    		medical installed products. Data driven decision making needs to diverge from the often-used subjective
    		approaches. In investment planning, the assets need to be assessed on key (performance) metrics to
    		determine the actions need to be taken for assets that need to be prioritized for replacement. The
    		Inventory Assessment Dashboard allows for a more automated assessment of the lifecycle status of the
    		fleet and advice on an end-result for replacement prioritization.

    		In asset management and CAPEX related planning, assets need to be assessed on key (performance)
    		metrics to understand replacement prioritization. Within the Assessment Dashboard the installed products
    		are scored on a set of assessment criteria in which the higher the score indicates a more vulnerable asset
    		and alludes to a higher nomination for replacement.

    		Many of the assessment scores are derived from the KPIs in the Inventory Dashboard. The assessment
    		result score is the rounded up average score for the installed product based on the available assessment
    		metric values displayed in the other KPIs and graphs. This allows for a more automated assessment of the
    		lifecycle status of your fleet.',
            'https://acc.insights.philips.com/sense/app/46d5d06c-ec37-4a1c-893a-f51f988f43b5',
            'platform.admin@philips.com',
    		'2024-01-01 09:00:00.000 +0530',
            'http://d1u65tovhg2ye9.cloudfront.net/icons/Assessment.svg',
    		'',
            '3.0.4',
            '6c349e5b-c950-47e0-bb99-01b1b8922ad1',
            'platform administrator',
            '2024-01-01 09:00:00.000 +0530',
            '2024-01-01 09:00:00.000 +0530',
            '',
            '',
            'IN5CHS0729',
            (SELECT category_id FROM category WHERE category_name = 'IT App'),
    		false,
            false,
            (SELECT status_id FROM application_status WHERE status_name = 'Approved by Market/Solution Owner'),
            (SELECT deployment_id FROM deployment WHERE deployment_mode = 'Multi-tenant'),
            true,
            false,
            false,
            false
            );

    INSERT INTO application (application_id, application_name, short_description, long_description, url, registered_by,
    registered_datetime, icon, banner, version, owner_organization, approver, last_modified_datetime, published_datetime,
    l1_comments, l2_comments, cost_center, category_id, is_mfe, is_sso_enabled, status_id, deployment_id, is_active,
    promote_as_upcoming_app, promote_as_newapp, is_third_party
    )
    VALUES ('5e72dd70-3aa3-4b2d-b609-8594e6758368',
            'Sentinel',
            'Philips tool for Licensing & Entitlements Management (LEM) to help the Philips business propositions to manage the distribution, tracking, and enforcement of licenses and entitlements for their products and services.',
            'This application is particularly helpful for Philips businesses that offer software, digital content, or other intellectual property that requires licensing. Key features of the system includes:
    		1. **License Generation**: Creating and issuing licenses for products or services to customers.
    		2. **Entitlement Tracking**: Recording and managing which customers are entitled to use specific products, services, or resources, ensuring compliance with licensing agreements.
    		3. **Automated Renewals and Updates**: Managing the renewal process for expiring licenses and pushing updates or upgrades to licensed products.
    		4. **Compliance Monitoring**: Tracking usage to ensure that customers are using the products in accordance with the terms of their licenses, helping to prevent unauthorized use.
    		5. **License Activation and Deactivation**: Facilitating the activation of new licenses and the deactivation of expired or non-compliant licenses.
    		6. **Reporting and Analytics**: Offering insights and reporting capabilities to monitor license usage, identify trends, and optimize licensing strategies.
    		7. **Subscription Management**: Providing customers or Philips Markets personal like Key Account Managers (KAMs) to manage licenses, view entitlements, manage subscriptions and perform actions like renewals or upgrades.
    		8. **Integration with Other Systems**: Is integrated with CRM, ERP, and other business management systems to streamline operations and ensure data consistency across the organization. Also, integrated with billing systems like Zuora, payment gateways like Adyen, revenue recognition with Philips commercial kernel, etc.
    		A Licensing & Entitlements Management system helps businesses protect their intellectual property, streamline administrative processes, enhance customer satisfaction, and ensure compliance with licensing terms.',
    		'https://e2esbxphilips.dev.sentinelcloud.com/admin/login',
            'platform.admin@philips.com',
    		'2024-01-01 09:00:00.000 +0530',
            'http://d1u65tovhg2ye9.cloudfront.net/icons/Sentinel.svg',
    		'',
            '1.0',
            null,
    		'platform administrator',
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
            false,
            false
            );

    INSERT INTO application (application_id, application_name, short_description, long_description, url, registered_by,
    registered_datetime, icon, banner, version, owner_organization, approver, last_modified_datetime, published_datetime,
    l1_comments, l2_comments, cost_center, category_id, is_mfe, is_sso_enabled, status_id, deployment_id, is_active,
    promote_as_upcoming_app, promote_as_newapp, is_third_party
    )
    VALUES ('5e72dd70-3aa3-4b2d-b609-8594e6758369',
            'HSP IAM Self Service',
            'The IAM Self-Service application provides the ability to manage identities of various types through a graphical
    		 user interface. It leverages REST APIs provided by HSP IAM, provides intuitive and easy to use graphical
    		 abstraction over IAM and provides basic user interaction elements.',
    		'The HSP IAM Self-Service application provides the following Identity & Access Management capabilities -
    		1. Create, update (limited support), read, and list operations on the following identity types -
    			a. Sub-organizations, Groups, Roles, Users (supports update) with or without temporary password and preferred language
    			b. Propositions, Applications, Service Identities, OAuth2.0 Client (supports update)
    			c. Platform Services Templates
    		2. View user account status, password status and membership details
    		3. View account lockout status and unlock user accounts
    		4. Delete a group, user account, application and service identity
    		5. Delete an organization identity and its resources
    		6. Bulk create roles, assign a role to a group, permissions to the role, and users to the group using a Platform Services Template
    		7. Enable/disable multi-factor authentication at the organization level
    		8. Update password validity, history and complexity at the organization level
    		9. All identities can be searched with a case sensitive keyword, a phrase match, or an exact match
    		10. Suspend/resume an organization from the organization details page. Resources under a suspended organization will not be accessible the organization is activated again
    		11. View "My account details" and Change login ID directly',
            'https://6jksere0j3.execute-api.us-east-1.amazonaws.com/client-test/api/authorize?client_id=SSClientForIAMEXT&redirect_uri=https://iamss-for-epo.us-east.philips-healthsuite.com/authorize',
            'platform.admin@philips.com',
    		'2024-01-01 09:00:00.000 +0530',
            'http://d1u65tovhg2ye9.cloudfront.net/icons/HSPIAMSelfService.svg',
    		'',
            '1.0',
            null,
            'platform administrator',
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
            false,
            false
            );

    INSERT INTO application (application_id, application_name, short_description, long_description, url, registered_by,
    registered_datetime, icon, banner, version, owner_organization, approver, last_modified_datetime, published_datetime,
    l1_comments, l2_comments, cost_center, category_id, is_mfe, is_sso_enabled, status_id, deployment_id, is_active,
    promote_as_upcoming_app, promote_as_newapp, is_third_party
    )
    VALUES ('5e72dd70-3aa3-4b2d-b609-8594e6758371',
            'User Management',
            'The HSP IAM User Management application provides various capabilities to manage users within the IAM landscape,
             through a graphical user interface.',
            'The following User management capabilities are supported by the application -
            1. Create user (support for login id different from email id)
            2. Self-registration, administrator-led registration
            3. Reset forgot password
            4. Change the user login name/login id
            5. Setup the secret question and answers for the KBA authentication
            6. Ability to choose 2FA if Organization doesn’t enforce by default',
            'https://usermanagementportal1.s3.amazonaws.com',
            'platform.admin@philips.com',
            '2024-01-01 09:00:00.000 +0530',
            'http://d1u65tovhg2ye9.cloudfront.net/icons/UserManagement.svg',
            '',
            '1.0',
            null,
            'platform administrator',
            '2024-01-01 09:00:00.000 +0530',
            '2024-01-01 09:00:00.000 +0530',
            '',
            '',
            'IN5CHS0703',
            (SELECT category_id FROM category WHERE category_name = 'Admin App'),
            true,
            false,
            (SELECT status_id FROM application_status WHERE status_name = 'Approved
            by Market/Solution Owner'),
            (SELECT deployment_id FROM deployment WHERE deployment_mode = 'Multi-tenant'),
            true,
            false,
            false,
            false
            );

    INSERT INTO application (application_id, application_name, short_description, long_description, url, registered_by,
    registered_datetime, icon, banner, version, owner_organization, approver, last_modified_datetime, published_datetime,
    l1_comments, l2_comments, cost_center, category_id, is_mfe, is_sso_enabled, status_id, deployment_id, is_active,
    promote_as_upcoming_app, promote_as_newapp, is_third_party
    )
    VALUES ('8a1bf01b-d543-4aab-83a8-b466c6234f05',
            'Dose Management',
            'This dashboard provides insights into radiation dose.',
            'In interventional guided therapy, the irradiation events need to be monitored and analyzed for regulatory
    		compliance monitoring and to maintain patient safety in medical imaging. Based on installed product logfile
    		data, the Dose Management Dashboard provides the daily dose management insights for Philips Azurion
    		installed products to effectively monitor on the dose level of radiation distribution (AirKerma, DAP and
    		Fluro time) being used per procedure, and per lab. It supports the inquiry within study and event level
    		details for the patients who are exposed to exceptional radiation cases. Medical Physicists are notified in
    		case of cumulative Air Kerma higher than specific defined limits. For all the patients exposed to radiation
    		dose above threshold for a particular study, the medical physicist is supported in analyzing and
    		understanding exceptional radiation studies and the X-ray acquisition and the dose within the radiation
    		field and area of tissue irradiated. The comparison in equipment conditions supports efforts in initiating
    		equipment correction, protocol improvements or recommend training for staff.
    		The Dose Management Dashboard is intended to be used by healthcare professionals to evaluate the
    		utilization and study details of their connected Image Guided Therapy installed products. Key performance
    		indicators (KPIs) are displayed utilizing near real-time logfile data, depending on enabled PRS connectivity
    		for your installed products.
    		The cloud-based Dose Management Dashboard is accessible 24x7 and assists in generating the required
    		reports when necessary.',
            '',
            'platform.admin@philips.com',
            '2024-01-01 09:00:00.000 +0530',
            'http://d1u65tovhg2ye9.cloudfront.net/icons/DoseManagement.svg',
            '',
            '1.0',
            null,
            'platform administrator',
            '2024-01-01 09:00:00.000 +0530',
            '2024-01-01 09:00:00.000 +0530',
            '',
            '',
            'IN5CHS0703',
            (SELECT category_id FROM category WHERE category_name = 'Admin App'),
            false,
            false,
            (SELECT status_id FROM application_status WHERE status_name = 'Approved
            by Market/Solution Owner'),
            (SELECT deployment_id FROM deployment WHERE deployment_mode = 'Multi-tenant'),
            true,
            false,
            false,
            false
            );



     RAISE NOTICE 'Data inserted successfully.';
EXCEPTION
    WHEN OTHERS THEN        
        ROLLBACK;        
        RAISE EXCEPTION 'An error occurred during the insertion. Transaction has been rolled back: %', SQLERRM;
END $$;
COMMIT;