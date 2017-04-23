'use strict';

// https://github.com/serverless/serverless
// https://github.com/pmuens/serverless-dynamodb-streams
const AWS = require('aws-sdk');

module.exports = {

  updateProfile: (event, context, callback) => {
    const dynamoDb = new AWS.DynamoDB.DocumentClient();

    const data = event;
    data.updatedAt = new Date().getTime();

    const params = {
      TableName: 'users',
      Item: data
    };

    return dynamoDb.put(params, (error, data) => {
      if (error) {
        callback(error);
      }
      callback(null, { message: 'Profile successfully updated', params });
    });
  },

  logger: (event, context, callback) => {
    // print out the event information on the console (so that we can see it in the CloudWatch logs)
    console.log(`The following happened in the DynamoDB database table "users":\n${JSON.stringify(event.Records[0].dynamodb, null, 2)}`);

    callback(null, { event });
  }
}
