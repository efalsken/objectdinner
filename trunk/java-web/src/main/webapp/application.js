var DATA_FORMATS = {
    DEFAULT_FORMAT:[
        {name: 'id'},
        {name: 'title'},
        {name: 'eventDate',type:'date',  dateFormat:'m/d/Y'},
        {name: 'eventTime',type:'date',  dateFormat:'H:i'},
        {name: 'description'},
        {name: 'hostedBy'},
        {name: 'attendeeCount'},
        {name: 'contactInfo'},
        {name: 'address'},
        {name: 'country'},
        {name: 'latitude'},
        {name: 'longitude'}
    ],
    READ_FORMAT:[
        {name: 'id'},
        {name: 'title'},
        {name: 'eventDate'},
        {name: 'eventTime'},
        {name: 'description'},
        {name: 'hostedBy'},
        {name: 'attendeeCount'},
        {name: 'contactInfo'},
        {name: 'address'},
        {name: 'country'},
        {name: 'latitude'},
        {name: 'longitude'}
    ]
};


function dinnerJSONReader(dataFormat) {
    if (!dataFormat) {
        dataFormat = DATA_FORMATS.DEFAULT_FORMAT;
    }
    return new Ext.data.JsonReader({
        root: 'rows' ,
        id: 'id',
        totalRecords: 'results'
    }, dataFormat);
}

function showList(mapControl) {

    var grid = new Ext.grid.GridPanel({
        store: new Ext.data.Store({
            url: 'listDinners.json',
            reader: dinnerJSONReader()}
                ),
        columns: [
            {header: "Attend",
                width: 60,
                dataIndex: 'id',
                sortable: false,
                xtype:'templatecolumn',
                tpl:'<a href="#attend{id}" onclick="attendAt({id})">Attend</a>'},
            {header: "Attendees", width: 50, dataIndex: 'attendeeCount', sortable: true} ,
            {header: "Title", width: 100, dataIndex: 'title', sortable: true} ,
            {header: "Date", width: 75, dataIndex: 'eventDate', sortable: true,xtype:'datecolumn',format: 'M/d/Y'} ,
            {header: "Host Name", width: 100, dataIndex: 'hostedBy', sortable: true}
        ],
        width: '100%',
        height: 500,
        defaults: {width: '90%'}
    });

    grid.store.on('load', function(store, records, options) {
        for (var i in records) {
            var record = records[i];
            if (typeof(record) === 'object') {
                mapControl.addPoint({
                    latitude:record.data.latitude,
                    longitude:record.data.longitude,
                    title:record.data.title,
                    id:record.data.id
                });
            }
        }
    });

    grid.store.load();


    return grid;
}

function showLoginPanel() {
    var openIdLoginForm = new Ext.FormPanel({
        standardSubmit: true,
        url: 'openid/startAuth.html',
        labelWidth: 90,
        frame:true,
        title: 'Please log in with your OpenId',
        bodyStyle:'padding:5px 5px 0',
        width: '100%',
        defaults: {width: '90%'},
        defaultType: 'textfield',
        monitorValid:true, items: [
            {
                fieldLabel: 'OpenId',
                name: 'openIdUrl',
                allowBlank:false,
                listeners: {
                    specialkey: function(field, e) {
                        if (e.getKey() == e.ENTER) {
                            var form = field.ownerCt.getForm();
                            form.submit();
                        }
                    }
                }
            }
        ]});
    openIdLoginForm.getForm().standardSubmit = true;
    openIdLoginForm.addButton({
        text: 'Login',
        enabled:false,
        handler: function() {
            openIdLoginForm.getForm().submit();
        }
    });
    return openIdLoginForm;
}

function showEnterAttendeeNamePanel(idToAttend) {
    var rsvpForm = new Ext.FormPanel({
        labelWidth: 90,
        frame:true,
        title: 'RSVP',
        bodyStyle:'padding:5px 5px 0',
        width: '100%',
        defaults: {width: '90%'},
        defaultType: 'displayfield',
        monitorValid:true,

        standardSubmit: true,
        reader : dinnerJSONReader(DATA_FORMATS.READ_FORMAT),
        url:'/rsvp/attend.html',
        items: [
            {
                xtype:'hidden',
                fieldLabel: 'id-field',
                name: 'id',
                allowBlank:false,
                value:idToAttend

            },
            {
                xtype:'textfield',
                fieldLabel: 'Your Name',
                name: 'name',
                allowBlank:false
            },
            {
                xtype:'label',
                fieldLabel: '<strong>Attending to</strong>'
            },
            {
                fieldLabel: 'Title',
                name: 'title',
                allowBlank:false
            },
            {
                fieldLabel: 'Date',
                name: 'eventDate'
            },
            {
                fieldLabel: 'Time',
                format:'H:m',
                name: 'eventTime',
                readOnly:true
            },
            {
                fieldLabel: 'Description',
                name: 'description'
            },
            {
                fieldLabel: 'Host Name',
                name: 'hostedBy'
            },
            {
                fieldLabel: 'Contact Info',
                name: 'contactInfo'
            },
            {
                fieldLabel: 'Address',
                name: 'address'
            },
            {
                fieldLabel: 'Country',
                name: 'country'
            }
        ]});


    rsvpForm.addButton({
        text: 'Attend to this dinner',
        enabled:false,
        handler: function() {
            rsvpForm.getForm().submit();
        }
    });

    var toLoad = 'dinner-' + idToAttend + '.json';
    rsvpForm.getForm().load(
    {url:toLoad,waitMsg:'Loading'});
    return rsvpForm;

}

function showAttendPanel(idToAttend) {
    if (dinnerApp.logginState) {
        return showEnterAttendeeNamePanel(idToAttend)
    } else {
        return showLoginPanel();
    }

}

function showHostPanel(idToLoad, mapControl) {
    var langitudeField = new Ext.form.Hidden(
    {
        fieldLabel: 'latitude',
        name: 'latitude',
        allowBlank:true
    });
    var longitudeField = new Ext.form.Hidden(
    {
        fieldLabel: 'longitude',
        name: 'longitude',
        allowBlank:true
    });

    var addressField = new Ext.form.TextArea(
    {
        fieldLabel: 'Address',
        name: 'address' ,
        allowBlank:false
    });

    addressField.on('blur', function() {
        langitudeField.setValue('');
        longitudeField.setValue('');
        mapControl.findLocation(this.getValue(), function(position) {
            langitudeField.setValue(position.lat());
            longitudeField.setValue(position.lng());
        });
    });

    var hostDinnerForm = new Ext.FormPanel({
        labelWidth: 90,
        frame:true,
        title: 'Host Dinner',
        bodyStyle:'padding:5px 5px 0',
        width: '100%',
        defaults: {width: '90%'},
        defaultType: 'textfield',
        monitorValid:true,
        standardSubmit: true,
        url:'saveDinner.json',

        // configure how to read the XML Data
        reader : dinnerJSONReader(),


        items: [
            {
                xtype:'hidden',
                fieldLabel: 'id-field',
                name: 'id',
                allowBlank:false
            },
            {
                fieldLabel: 'Title',
                name: 'title',
                allowBlank:false
            },
            {
                xtype:'datefield',
                fieldLabel: 'Date',
                name: 'eventDate',
                allowBlank:false
            },
            {
                xtype:'timefield',
                fieldLabel: 'Time',
                name: 'eventTime',
                format:'H:m',
                allowBlank:false
            },
            {
                xtype:'textarea',
                fieldLabel: 'Description',
                name: 'description'
            },
            {
                fieldLabel: 'Host Name',
                name: 'hostedBy',
                allowBlank:false
            },
            {
                fieldLabel: 'Contact Info',
                name: 'contactInfo'
            },
            addressField,
            langitudeField,
            longitudeField,
            {
                fieldLabel: 'Country',
                name: 'country'
            }
        ]
    });

    var submitButton = hostDinnerForm.addButton({
        text: 'Submit',
        enabled:false,
        handler: function() {
            hostDinnerForm.getForm().submit();
        }
    });
    hostDinnerForm.on({
        clientvalidation: function(form, valid) {
            if (valid) {
                submitButton.enable();
            } else {
                submitButton.disable();
            }
        }
    });
    var toLoad = 'newDinner.json';
    if (idToLoad) {
        toLoad = 'dinner-' + idToLoad + '.json';
    }

    hostDinnerForm.getForm().load(
    {url:toLoad,waitMsg:'Loading'});


    return hostDinnerForm;
}

var showPanel = function() {
    var currentPanel = null;

    return function(panelFactory) {
        if (currentPanel) {
            currentPanel.destroy();
        }
        currentPanel = panelFactory();
        currentPanel.render('application-container');
    }
}();

function attendAt(idToEdit) {
    showPanel(function() {
        return showAttendPanel(idToEdit)
    });
}

function renderLogin() {
    new Ext.FormPanel()
}

function createMap() {
    var latlng = new google.maps.LatLng(40, 5);
    var myOptions = {
        zoom: 2,
        center: latlng,
        mapTypeId: google.maps.MapTypeId.ROADMAP
    };
    var geocoder = new google.maps.Geocoder();
    var map = new google.maps.Map(document.getElementById("mapView"),
            myOptions);

    var temporaryMarker = null;

    function cleanMarker() {
        if (temporaryMarker) {
            temporaryMarker.setMap(null);
        }
    }

    return {
        addPoint:function(pointToAdd) {
            var marker = new google.maps.Marker({
                map: map,
                position: new google.maps.LatLng(pointToAdd.latitude, pointToAdd.longitude),
                title:pointToAdd.title
            });
            google.maps.event.addListener(marker, 'click', function() {
                attendAt(pointToAdd.id);
            });
        },

        findLocation:function(addressToLocate, storeLocationFunction) {
            geocoder.geocode({ 'address': addressToLocate}, function(results, status) {
                cleanMarker();
                if (status == google.maps.GeocoderStatus.OK) {
                    var locationInfo = results[0].geometry.location;
                    map.setCenter(locationInfo);
                    map.setZoom(14);
                    temporaryMarker = new google.maps.Marker({
                        map: map,
                        position: results[0].geometry.location,
                        title:addressToLocate
                    });
                    storeLocationFunction(locationInfo);
                } else {
                    Ext.Msg.alert('Address', "Couldnt find Address, sorry");
                }
            });
        }
    }
}

Ext.onReady(function() {

    var mapControl = createMap();
    Ext.Ajax.defaultHeaders = {
        'Accept': 'application/json;charset=utf-8'
    };

    function newHostPanelWithEmptyMap() {
        return showHostPanel(null, mapControl);
    }

    var action = location.hash;
    if ('#host' === action) {
        showPanel(newHostPanelWithEmptyMap);
    } else if (0 === action.indexOf("#edit")) {
        showAttendPanel(action.substring(5));
    } else if (0 === action.indexOf("#attend")) {
        attendAt(action.substring(7));
    } else {
        showPanel(function() {
            return showList(mapControl);
        });
    }


    var button = Ext.get('hostButton');
    if (button) {
        button.on('click', function() {
            showPanel(newHostPanelWithEmptyMap);
        });
    }
});