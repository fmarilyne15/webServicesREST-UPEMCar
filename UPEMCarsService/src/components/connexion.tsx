import { withStyles, WithStyles } from '@material-ui/core/styles';

import Button from '@material-ui/core/Button';
import InputBase from '@material-ui/core/InputBase';
import Paper from '@material-ui/core/Paper';
import Typography from '@material-ui/core/Typography';

import * as React from 'react';

const decoratedConnexion = withStyles(theme => ({
    button: {
        backgroundColor: '#EA5A0B',
        color: "#FFF",
        marginRight: '5%',
    },
    inputBase: {
        width: theme.spacing.unit * 50,
    },
    paper: {
        marginLeft: '35%',
        marginTop: '20%',
        width: '30%'
    },
    typography: {
        backgroundColor: '#EA5A0B',
        color: "#FFF",
    }
}));

interface IConnexionProps{
    readonly login: (event: any) => void;
    readonly password: (event: any) => void;
    readonly connexion: () => void;
}

export const ConnexionCustomized = decoratedConnexion(class extends React.Component<IConnexionProps & WithStyles<'button' | 'inputBase' | 'paper' | 'typography'>>{

    public render() {
        const { classes } = this.props;
        return (
            <Paper className={classes.paper} elevation={24} square={true}>
                <Typography className={classes.typography}> Connectez-vous </Typography>
                <InputBase className={classes.inputBase} placeholder="Login" onChange={this.props.login} autoFocus={true}/> <br /> <br />
                <InputBase type='password' className={classes.inputBase} placeholder="Mot de passe" onChange={this.props.password} />
                <Button onClick={this.props.connexion} className={classes.button}>
                    Connexion
                </Button>
            </Paper>
        )
    }
});
