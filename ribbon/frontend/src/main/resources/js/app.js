var Ribbon = ribbon();

var App = React.createClass({
  render: function() {
    return (
        <div>
          <h1>WildFly Swarm Ribbon Example</h1>
          <Topology />
        </div>
    );
  }
});

var Topology = React.createClass({
  getInitialState: function() {
    return {data: []};
  },

  componentDidMount: function() {
    var component = this;
    Ribbon.topologyEvents.addEventListener('topologyChange', function(message) {
      component.setState({data: JSON.parse(message.data)});
    });
  },

  render: function() {
    var services = [];
    for (var service in this.state.data) {
      var addresses = this.state.data[service];
      services.push(
        (
            <Service service={service} addresses={addresses} key={service} />
        )
      );
    }
    return (
        <div id='topology'>
        <h2>Service Topology</h2>
        {services}
        </div>
    );
  }
});

var Service = React.createClass({
  render: function() {
    var addresses = this.props.addresses.map(function(address) {
      return (
          <Address address={address} key={address}/>
      );
    });
    return (
      <div className='service'>
        <h2>{this.props.service}</h2>
        {addresses}
        <DataButton serviceName={this.props.service} />
      </div>
    );
  }
});

var DataButton = React.createClass({
  getInitialState: function() {
    return {response: ''};
  },

  handleClick: function(event) {
    var button = this;
    console.log("Calling service: " + this.props.serviceName);
    Ribbon.getJSON(this.props.serviceName)
      .promise
      .then(function(response) {
        console.log("Got response ")
        console.log(response);
        button.setState({
          response: JSON.stringify(response)
        });
      });
  },

  render: function() {
    return (
      <div>
        <p onClick={this.handleClick}>
        Click to request {this.props.serviceName}
        </p>
        <p>
        {this.state.response}
        </p>
      </div>
    );
  }
});

var Address = React.createClass({
  render: function() {
    return (
        <p className='service-address'>{this.props.address}</p>
    );
  }
});

ReactDOM.render(
  <App />,
  document.getElementById('app')
);
