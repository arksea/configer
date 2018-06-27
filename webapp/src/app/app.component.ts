import { Component } from '@angular/core';
import {
  WidgetRegistry,
  Validator,
  DefaultWidgetRegistry,
} from 'ngx-schema-form';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css'],
  providers: [{ provide: WidgetRegistry, useClass: DefaultWidgetRegistry }]
})
export class AppComponent {
  schema: any;
  model: any;
  value: any;
  actions = {};

  constructor(registry: WidgetRegistry) {
    this.schema = {
      '$schema': 'http://json-schema.org/draft-04/hyper-schema#',
      'type': 'object',
      'properties': {
        'firstName': {
          'type': 'string',
          'placeholder': 'First name',
          'minLength': 2,
          'maxLength': 40,
          'title': 'First name',
          'description': 'First name'
        },
        'lastName': {
          'type': 'string',
          'placeholder': 'Last name',
          'minLength': 2,
          'maxLength': 40,
          'title': 'Last name',
          'description': 'Last name'
        },
        'bornOn': {
          'type': 'string',
          'format': 'date',
          'widget': 'date',
          'default': '1800-12-12',
          'placeholder': 'Ex: 2009-03-11',
          'description': 'Born on'
        },
        'categories': {
          'type': 'array',
          'items': {
            'type': 'string',
            'oneOf': [
              {
                'description': 'Dog',
                'enum': [
                  'dog'
                ]
              },
              {
                'description': 'Cat',
                'enum': [
                  'cat'
                ]
              },
              {
                'description': 'Daulphin',
                'enum': [
                  'daulphin'
                ]
              }
            ]
          },
          'widget': 'checkbox'
        },
        'moreInfo': {
          'type': 'boolean',
          'widget': 'checkbox',
          'description': 'More information?',
          'default': true
        },
        'survey': {
          'type': 'object',
          'description': 'Little survey',
          'properties': {
            'q1': {
              'type': 'string',
              'description': 'Enter a number'
            },
            'q2': {
              'type': 'object',
              'description': 'Address',
              'properties': {
                'color': {
                  'description': 'color',
                  'type': 'string',
                  'default': '#aaa000',
                  'pattern': 'ff$',
                  'widget': 'color'
                },
                'zip': {
                  'description': 'zip',
                  'type': 'number',
                  'default': 15
                }
              }
            }
          }
        },
        'favoriteColor': {
          'type': 'string',
          'pattern': '^#[0-9a-fA-F]{6}$',
          'widget': 'color',
          'default': '#aaa111',
          'description': 'Favorite color',
          'visibleIf': {
            'moreInfo': [
              true
            ]
          }
        },
        'transactionNumber': {
          'type': 'integer',
          'minimum': 0,
          'readOnly': 'true',
          'description': 'Transaction number'
        },
        'transactionDescription': {
          'type': 'string',
          'widget': 'textline',
          'description': 'What is being transacted'
        },
        'category': {
          'type': 'array',
          'widget': 'select',
          'items': {
            'type': 'string',
            'oneOf': [
              {
                'description': 'Design',
                'enum': [
                  'design'
                ]
              },
              {
                'description': 'High-Tech',
                'enum': [
                  'hightech'
                ]
              },
              {
                'description': 'Materials',
                'enum': [
                  'materials'
                ]
              },
              {
                'description': 'Services',
                'enum': [
                  'services'
                ]
              }
            ]
          },
          'description': 'Category'
        },
        'promotion': {
          'type': 'string',
          'description': 'Promotion',
          'widget': 'radio',
          'oneOf': [
            {
              'description': 'Student discount (20%)',
              'enum': [
                'student'
              ]
            },
            {
              'description': 'Summer 2016 discount (15%)',
              'enum': [
                'summer'
              ]
            },
            {
              'description': 'None',
              'enum': [
                'none'
              ]
            }
          ]
        },
        'confirmationMail': {
          'type': 'string',
          'description': 'Email',
          'format': 'email'
        },
        'password': {
          'type': 'string',
          'widget': 'password',
          'description': 'Password'
        },
        'numberOfBoxes': {
          'type': 'number',
          'widget': {
            'id': 'range'
          },
          'description': 'Number of boxes required',
          'minimum': 1,
          'maximum': 10
        },
        'deliveryService': {
          'type': 'string',
          'widget': 'select',
          'description': 'Delivery service',
          'oneOf': [
            {
              'description': 'UPS',
              'enum': [
                'ups'
              ]
            },
            {
              'description': 'FedEx',
              'enum': [
                'fedex'
              ]
            },
            {
              'description': 'Other',
              'enum': [
                'other'
              ]
            }
          ],
          'default': 'fedex'
        },
        'otherDeliveryService': {
          'type': 'string',
          'minLength': 2,
          'visibleIf': {
            'deliveryService': [
              'other'
            ]
          }
        },
        'freeShipping': {
          'type': 'boolean',
          'widget': 'checkbox',
          'description': 'Free shipping',
          'visibleIf': {
            'deliveryService': [
              'other',
              'ups'
            ]
          }
        },
        'shippingPrice': {
          'type': 'number',
          'description': 'ShippingCost',
          'minimum': 0,
          'maximum': 200,
          'visibleIf': {
            'freeShipping': [
              false
            ]
          }
        },
        'sendApologies': {
          'type': 'boolean',
          'widget': 'checkbox',
          'description': 'Send apologies for the shipping cost',
          'visibleIf': {
            'shippingPrice': [
              22,
              23
            ]
          }
        },
        'useCustomEmail': {
          'type': 'boolean',
          'description': 'Write a custom email ?'
        },
        'customEmail': {
          'type': 'string',
          'widget': 'textarea',
          'description': 'Email to send',
          'visibleIf': {
            'useCustomEmail': [
              true
            ]
          },
          'pattern': '^<h1>'
        },
        'userManual': {
          'type': 'object',
          'widget': 'file',
          'properties': {
            'content-type': {
              'type': 'string'
            },
            'filename': {
              'type': 'string'
            },
            'size': {
              'type': 'integer'
            },
            'encoding': {
              'type': 'string'
            },
            'data': {
              'type': 'string'
            }
          },
          'description': 'Add a manual for the delivered items',
          'visibleIf': {
            'category': [
              'hightech'
            ]
          }
        },
        'colors': {
          'type': 'array',
          'description': 'Colors',
          'index': 'i',
          'items': {
            'type': 'string',
            'description': 'Color $i',
            'widget': 'color',
            'buttons': [
              {
                'label': 'Supprimer',
                'id': 'Remove'
              }
            ]
          },
          'buttons': [
            {
              'label': 'Ajouter',
              'id': 'addItem',
              'parameters': {
                'value': '#afeadd'
              }
            },
            {
              'label': 'Reset',
              'id': 'reset'
            }
          ]
        }
      },
      'buttons': [
        {
          'label': 'Alert',
          'id': 'alert'
        },
        {
          'label': 'Reset',
          'id': 'reset'
        },
        {
          'label': 'Disable all',
          'id': 'disable'
        }
      ],
      'fieldsets': [
        {
          'id': 'part_1',
          'title': 'Part 1 - Recipient',
          'fields': [
            'firstName',
            'lastName',
            'categories',
            'bornOn',
            'moreInfo',
            'favoriteColor',
            'colors',
            'survey'
          ]
        },
        {
          'id': 'part_2',
          'title': 'Part 2 - Transaction',
          'fields': [
            'transactionNumber',
            'transactionDescription',
            'promotion',
            'category',
            'userManual'
          ]
        },
        {
          'id': 'part_3',
          'title': 'Part 3 - Shipping',
          'fields': [
            'numberOfBoxes',
            'deliveryService',
            'otherDeliveryService',
            'freeShipping',
            'shippingPrice',
            'sendApologies'
          ]
        },
        {
          'id': 'part_4',
          'title': 'Part 4 - Email',
          'fields': [
            'useCustomEmail',
            'customEmail'
          ]
        },
        {
          'id': 'part_5',
          'title': 'Part 5 - Confirmation',
          'fields': [
            'confirmationMail',
            'password'
          ]
        }
      ],
      'required': [
        'firstName',
        'lastName',
        'transactionNumber',
        'password'
      ]
    };

    this.actions['alert'] = (property, options) => {
        property.forEachChildRecursive(child => {
            console.log(child.valid, child);
        });
        alert(JSON.stringify(property.value));
    };

    this.actions['reset'] = (form, options) => {
        form.reset();
    };
    this.actions['reset'] = (form, options) => {
        form.reset();
    };
    this.actions['disable'] = this.disableAll.bind(this);
    }
    logErrors(errors) {
      console.log('ERRORS', errors);
    }
    setValue(value) {
      this.value = value;
    }
    disableAll() {
      Object.keys(this.schema.properties).map(prop => {
          this.schema.properties[prop].readOnly = true;
      });
  }
}
